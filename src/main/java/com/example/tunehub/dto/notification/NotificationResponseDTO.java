package com.example.tunehub.dto.notification;

import com.example.tunehub.dto.user.UsersProfileDTO;

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
