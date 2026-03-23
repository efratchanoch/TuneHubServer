package com.example.tunehub.dto.post;


import java.time.LocalDateTime;

public record PostSearchDTO(
        Long id,
        String title,
        String userName,
        LocalDateTime dateUploaded
) {
}
