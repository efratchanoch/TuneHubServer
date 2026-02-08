package com.example.tunehub.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record SheetMusicSearchDTO (
        Long id,
        String title,
        String userName,
        LocalDateTime dateUploaded,
        String imageCoverName
){ }

