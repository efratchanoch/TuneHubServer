package com.example.tunehub.dto.sheetmusic;

import java.util.List;

public record SheetMusicResponseAI(
        String title,
        String scale,
        List<String> instruments,
        String difficulty,
        List<String> suggestedCategories,
        String composer,
        String lyricist
) {}
