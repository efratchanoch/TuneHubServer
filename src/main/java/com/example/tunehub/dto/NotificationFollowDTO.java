package com.example.tunehub.dto;

import com.example.tunehub.model.Users;

import java.time.LocalDateTime;

public record NotificationFollowDTO(
        LocalDateTime dateTime,
        Long userId,
        Users follow,
        boolean isRead
) {}
