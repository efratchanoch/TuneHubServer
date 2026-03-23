package com.example.tunehub.dto.sheetmusic;

import com.example.tunehub.dto.common.InstrumentResponseDTO;

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
