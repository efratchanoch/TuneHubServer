package com.example.tunehub.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UsersLogInDTO {

    @NotBlank(message = "Username or email is required.")
    @Size(min = 3, max = 100, message = "Username/email length is invalid.")
    private String name;


    @NotBlank(message = "Password is required.")
    @Size(min = 8, message = "Password length is invalid.")
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
