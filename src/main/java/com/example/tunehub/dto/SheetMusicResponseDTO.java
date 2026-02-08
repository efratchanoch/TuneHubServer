package com.example.tunehub.dto;

import com.example.tunehub.model.EDifficultyLevel;
import com.example.tunehub.model.EScale;

import java.time.LocalDate;
import java.util.List;

public record SheetMusicResponseDTO (
        Long id,
        String title,
        List<InstrumentResponseDTO> instruments,
        List<SheetMusicCategoryResponseDTO> categories,
        EDifficultyLevel level,
        EScale scale,
        String filePath,
        UsersProfileDTO user,
        LocalDate dateUploaded,
        int downloads,
        int pages,
        String imageCoverName,
        double rating,
        boolean liked,
        boolean favorite,
        int hearts,
        int likes,
        String composer,
        String lyricist

){}

