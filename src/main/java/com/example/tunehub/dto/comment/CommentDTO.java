package com.example.tunehub.dto.comment;

import com.example.tunehub.dto.user.UsersProfileDTO;

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