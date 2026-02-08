package com.example.tunehub.dto;

import java.util.List;

public record SheetMusicFinalResponseAIDTO (
        String title,
        String scale,
        List<InstrumentResponseDTO> instruments,
        String difficulty,
        List<SheetMusicCategoryResponseDTO> suggestedCategories,
        String composer,
        String lyricist
){ }
