package com.example.tunehub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UsersUploadProfileImageDTO {

    @NotNull(message = "User ID is required for profile update.")
    private Long id;

    // imagePath is likely set by the server, but if it's used for deletion logic,
    // you might validate it. Assuming here it's an output or internal field.
    private String imagePath;

    @NotBlank(message = "Image data (Base64 or URL) is required for upload.")
    @Size(min = 10, max = 5242880, message = "Image data size is invalid.") // Example size constraint (5MB)
    private String image;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}