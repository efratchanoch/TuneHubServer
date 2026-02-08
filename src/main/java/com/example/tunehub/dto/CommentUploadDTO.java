
package com.example.tunehub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CommentUploadDTO (
        @NotBlank(message = "Comment content cannot be empty.")
        @Size(min = 5, max = 500, message = "Comment content must be between 5 and 500 characters.")
        String content,

        @NotNull(message = "Post ID is required.")
        Long postId
){}