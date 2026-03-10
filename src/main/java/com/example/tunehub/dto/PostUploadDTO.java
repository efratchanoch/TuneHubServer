package com.example.tunehub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PostUploadDTO (
        @NotBlank(message = "Post title cannot be empty.")
        @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters.")
        String title,

        @NotBlank(message = "Post content cannot be empty.")
        @Size(min = 10, max = 5000, message = "Content must be between 10 and 5000 characters.")
        String content

) {}