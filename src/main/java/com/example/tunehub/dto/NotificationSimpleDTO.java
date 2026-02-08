package com.example.tunehub.dto;

public record NotificationSimpleDTO(
        Long targetId,
        int count,
        boolean isRead
) {}
