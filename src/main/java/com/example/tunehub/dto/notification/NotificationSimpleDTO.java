package com.example.tunehub.dto.notification;

public record NotificationSimpleDTO(
        Long targetId,
        int count,
        boolean isRead
) {}
