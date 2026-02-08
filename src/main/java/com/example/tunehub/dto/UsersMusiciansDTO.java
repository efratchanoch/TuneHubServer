package com.example.tunehub.dto;

import com.example.tunehub.model.EUserType;

import java.time.LocalDate;
import java.util.Set;

public record UsersMusiciansDTO (
         UsersProfileDTO profile,
         String email,
         String city,
         String country,
         boolean isActive,
         String description,
         InstrumentResponseDTO instruments,
         Set<EUserType> userTypes,
         LocalDate createdAt,
         TeacherListingDTO teacher
){}

