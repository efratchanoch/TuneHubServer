package com.example.tunehub.service;

import com.example.tunehub.model.Users;
import com.example.tunehub.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsersRepository usersRepository;

    @Autowired
    public AuthService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User is not authenticated.");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetails) {
            Long userId = ((CustomUserDetails) principal).getId();
            if (userId == null || userId <= 0) {
                throw new IllegalStateException("Invalid user ID in Security Context.");
            }
            return userId;
        }
        throw new IllegalStateException("Principal is not of the expected type.");
    }

    public Users getCurrentUser() {
        Long userId = getCurrentUserId();
        return usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found."));
    }
}

