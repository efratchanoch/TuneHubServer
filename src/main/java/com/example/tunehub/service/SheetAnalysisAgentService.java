package com.example.tunehub.service;

import com.example.tunehub.dto.SheetMusicResponseAI;
import com.example.tunehub.model.Instrument;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Sends PDF bytes to model and expects strict JSON in English:
 * { "scale": "...", "instruments":[...], "difficulty":"..." , "suggestedCategoryName":"..." }
 * <p>
 * NOTE: embedding full base64 into the prompt works for small PDFs. For big PDFs use private URL or extract features server-side.
 */
@Service
public class SheetAnalysisAgentService {


    private final ChatClient chatClient;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final MusicMetadataSearchTool musicMetadataSearchTool;
    private final PdfTextExtractorService pdfTextExtractorService;
    private final InstrumentRepository instrumentRepository;
    private List<String> existingInstruments;
    private String instrumentsList;

    // Strong system prompt — placed as default system in builder (configured when ChatClient.Builder is created)
    // Make sure when you build ChatClient.Builder you call .defaultSystem(SYSTEM_PROMPT)
    public static final String SYSTEM_PROMPT = """
            You are an ultra-precise professional sheet-music analysis AI.
            INPUT: a PDF in base64 format.
            OUTPUT: JSON ONLY (English only) with the exact fields described below.
            This system analyzes instrumental sheet music for musicians, NOT commercial songs.
            
            ===================================================
            REQUIRED FIELDS
            ===================================================
            
            "title":
                Extract EXACTLY from the sheet music.
                If written in Hebrew or any non-Latin script, transliterate LETTER-BY-LETTER into English.
                Never translate by meaning.
                Never replace with a known melody or prayer.
                Never infer from musical similarity.
                If the PDF does not show any clear title, return an empty string.
            
            "scale":
                Determine the key ONLY by the visible key signature and tonal center in the PDF.
                If uncertain, return an empty string.
                Never guess.
            
            "instruments":
                Return ALL instruments that realistically match the composition.
                Only choose instruments from the following list: [Piano, Guitar, Violin, Clarinet, Flute, Trumpet].
                No limit on quantity.
                No variants (no “Acoustic…”, “Electric…”, “Classical…”, “Jazz…”).
                No duplicates.
                Never invent new instruments.
                Never create categories related to instruments.
            
            "difficulty":
                BEGINNER, INTERMEDIATE, or ADVANCED.
            
            ===================================================
            OPTIONAL FIELDS
            ===================================================
            
            "composer":
                1. First extract EXACTLY from the PDF itself (metadata, title page, notes).
                    2. If missing, **YOU MUST CALL THE 'searchForMetadata' TOOL** using the exact 'title' field as input.
                    3. Use the value returned in the 'foundComposer' field by the tool. If still not found or the tool fails, return an empty string.
                    4. If written in Hebrew or another script, transliterate letter-by-letter.
                    5. Never assign "Traditional" unless verified.
            
            "lyricist":
               These pieces are instrumental unless lyrics appear in the PDF.
                   1. If lyrics exist, extract and transliterate.
                   2. If missing, check the value returned in the 'foundLyricist' field by the 'searchForMetadata' tool call.
                   3. If the tool returns a name, use it. If the tool returns "No Lyricist" or an empty string, return "No Lyricist".
            
            "suggestedCategories":
                Choose up to 3 categories from the existing database based ONLY on:
                    • style
                    • genre
                    • musical character
                    • harmony
                    • rhythm
                Never use lifestyle categories.
                Never create instrument-based categories.
                If no category fits, you may create up to 2 new ones (max total = 5).
                Return in order of relevance.
            
            ===================================================
            STRICT RULES
            ===================================================
            
            ABSOLUTE NO-GUESSING POLICY:
                • Never guess the title.
                • Title MUST come only from PDF text.
                • Never infer from melody, style, religion, theme, common Jewish tunes, or similar musical patterns.
                • Never replace the title with another known composition (e.g., never substitute “Ma’oz Tzur”).
                • Never guess composer or lyricist. Use ONLY PDF or verified online sources.
                • If no reliable source exists, return empty fields.
            
            CONTENT-MATCHING RESTRICTION:
                • You are forbidden from identifying the piece based on melody similarity, motifs,
                  or cultural associations.
                • Use ONLY written textual information and verified external metadata.
            
            TRANSLITERATION RULE:
                • Hebrew → English must be strict letter-by-letter transliteration.
                • Never translate meaning.
                • Examples: אחינו → Acheinu ; נרות שבת → Nerot Shabat.
            
            FORMAT RULE:
                • Output VALID JSON ONLY.
                • No explanations.
                • No additional text.
            """;

    @Autowired
    public SheetAnalysisAgentService(ChatClient.Builder builder,
                                     MusicMetadataSearchTool musicMetadataSearchTool,
                                     PdfTextExtractorService pdfTextExtractorService,
                                     InstrumentRepository instrumentRepository
    ) {
        this.musicMetadataSearchTool = musicMetadataSearchTool;
        this.pdfTextExtractorService = pdfTextExtractorService;
        this.instrumentRepository = instrumentRepository;

        this.chatClient = builder
                .defaultSystem(SYSTEM_PROMPT)
                .defaultTools(musicMetadataSearchTool)
                .build();
    }

    public SheetMusicResponseAI analyzePdfBytes(byte[] pdfBytes) throws Exception {
        if (pdfBytes == null || pdfBytes.length == 0) return null;

        // Extract the text and internal title using your service
        PdfTextExtractorService.ExtractResult extractResult = pdfTextExtractorService.extract(pdfBytes);
        String extractedText = extractResult.text;
        String titleCandidate = extractResult.titleCandidate;

        if (extractedText.length() < 50) {
            throw new RuntimeException("Failed to extract sufficient text from PDF/OCR.");
        }

        String prompt = "Analyze the following Sheet Music. Use the provided text content and the title candidate to fill the JSON fields. " +
                "**Crucial Instruction: If 'composer' or 'lyricist' are missing from the PDF text, you must first call the 'searchForMetadata' tool using the extracted 'title' before finalizing the JSON response.** " +
                "Return JSON ONLY with all required fields.\n\n" +
                "Title Candidate: " + titleCandidate + "\n\n" +
                "Sheet Music Text Content:\\n" + extractedText;

        UserMessage userMessage = new UserMessage(prompt);

        this.existingInstruments = getAllInstrumentNames();
        this.instrumentsList = String.join(", ", existingInstruments);

        String raw = chatClient.prompt().messages(List.of(userMessage)).call().content();

        // Extract JSON substring
        int first = raw.indexOf('{');
        int last = raw.lastIndexOf('}');
        if (first < 0 || last <= first) {
            throw new RuntimeException("AI did not return JSON. Raw response: " + raw);
        }
        String json = raw.substring(first, last + 1);

        return objectMapper.readValue(json, SheetMusicResponseAI.class);
    }

    public List<String> getAllInstrumentNames() {
        return instrumentRepository.findAll()
                .stream()
                .map(Instrument::getName)
                .toList();
    }
}


