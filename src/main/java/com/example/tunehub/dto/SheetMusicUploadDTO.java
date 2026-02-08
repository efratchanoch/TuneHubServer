package com.example.tunehub.dto;

import com.example.tunehub.model.EDifficultyLevel;
import com.example.tunehub.model.EScale;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record SheetMusicUploadDTO(

        @NotBlank(message = "Title is required.")
        @Size(min = 3, max = 255, message = "Title must be between 3 and 255 characters.")
        String title,

        @NotNull(message = "At least one instrument is required.")
        @NotEmpty(message = "The instrument list cannot be empty.")
        List<InstrumentResponseDTO> instruments,

        @NotNull(message = "At least one category is required.")
        @NotEmpty(message = "The category list cannot be empty.")
        List<SheetMusicCategoryResponseDTO> categories,

        @NotNull(message = "Difficulty level is required.")
        EDifficultyLevel level,

        @NotNull(message = "Scale is required.")
        EScale scale,

        @NotBlank(message = "File name is required.")
        String fileName,

        // Assuming UsersDTO validation is handled separately if needed,
        // but the DTO object itself must not be null.
        @NotNull(message = "User information is required.")
        @Valid
        UsersDTO user,

        @NotBlank(message = "Composer is required.")
        @Size(max = 255, message = "Composer name cannot exceed 255 characters.")
        String composer,

        @NotBlank
        @Size(max = 255, message = "Lyricist name cannot exceed 255 characters.")
        String lyricist
) {}
