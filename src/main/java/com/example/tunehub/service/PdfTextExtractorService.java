package com.example.tunehub.service;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@Service
public class PdfTextExtractorService {

    private final ITesseract tess;

    public PdfTextExtractorService() {
        System.setProperty("TESSDATA_PREFIX", "C:\\Program Files\\Tesseract-OCR\\tessdata");

        tess = new Tesseract();
        tess.setDatapath("C:\\Program Files\\Tesseract-OCR\\tessdata");
        tess.setLanguage("heb+eng");
        tess.setTessVariable("preserve_interword_spaces", "1");
    }

    public ExtractResult extract(byte[] pdfBytes) throws IOException {
        try (PDDocument doc = PDDocument.load(new ByteArrayInputStream(pdfBytes))) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(doc).trim();

            String metadataTitle = doc.getDocumentInformation() != null ?
                    doc.getDocumentInformation().getTitle() : null;

            if (isTextSufficient(text)) {
                String title = pickTitleFromText(text, metadataTitle);
                return new ExtractResult(text, title, false);
            }

            PDFRenderer renderer = new PDFRenderer(doc);
            StringBuilder ocrAll = new StringBuilder();
            int pages = doc.getNumberOfPages();
            for (int i = 0; i < pages; i++) {
                BufferedImage image = renderer.renderImageWithDPI(i, 300);
                try {
                    String ocr = tess.doOCR(image);
                    if (ocr != null && !ocr.isBlank()) {
                        ocrAll.append(ocr).append("\n\n");
                    }
                } catch (TesseractException e) {
                    System.err.println("OCR failed on page " + (i+1) + ": " + e.getMessage());
                }
            }

            String ocrText = ocrAll.toString().trim();
            String title = pickTitleFromText(ocrText, metadataTitle);
            return new ExtractResult(ocrText, title, true);
        }
    }

    private boolean isTextSufficient(String text) {
        if (text == null || text.isBlank()) return false;
        int meaningful = text.replaceAll("\\s+", "").length();
        return meaningful > 80;
    }

    private String pickTitleFromText(String fullText, String metadataTitle) {
        if (metadataTitle != null && !metadataTitle.isBlank()) return metadataTitle.trim();

        String[] lines = fullText.split("\\r?\\n");
        for (String l : lines) {
            String s = l.trim();
            if (s.length() > 1 && !s.matches("^[\\d\\W]+$")) {
                if (s.matches(".*[\\p{L}].*")) {
                    return s;
                }
            }
        }
        return "";
    }

    public static class ExtractResult {
        public final String text;
        public final String titleCandidate;
        public final boolean ocrUsed;

        public ExtractResult(String text, String titleCandidate, boolean ocrUsed) {
            this.text = text;
            this.titleCandidate = titleCandidate;
            this.ocrUsed = ocrUsed;
        }
    }
}
