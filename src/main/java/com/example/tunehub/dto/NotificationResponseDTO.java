package com.example.tunehub.dto;

import java.time.OffsetDateTime;


public record NotificationResponseDTO(
        Long id,
        String title,
        String message,
        OffsetDateTime createdAt,
        boolean isRead,
        Long targetId,
        UsersProfileDTO actor,
        Integer count
) {}
