package com.example.tunehub.dto;

public record UsersSearchDTO(
        UsersProfileDTO profile,
        String country,
        String city
) {}
