package com.example.tunehub.dto.user;

public record UsersSearchDTO(
        UsersProfileDTO profile,
        String country,
        String city
) {}
