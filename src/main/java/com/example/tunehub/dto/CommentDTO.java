package com.example.tunehub.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;

public record CommentDTO(
        Long id,
        String content,
        OffsetDateTime dateUploaded,
        UsersProfileDTO profile,
        int likes,
        boolean isLiked
) {
}