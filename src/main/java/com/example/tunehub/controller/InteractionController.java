package com.example.tunehub.controller;

import com.example.tunehub.dto.FavoriteItemDTO;
import com.example.tunehub.dto.NotificationSimpleDTO;
import com.example.tunehub.model.EFollowStatus;
import com.example.tunehub.model.ETargetType;
import com.example.tunehub.service.AuthService;
import com.example.tunehub.service.InteractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interaction")
public class InteractionController {

    private final InteractionService interactionService;
    private final AuthService authService;

    @Autowired
    public InteractionController(InteractionService interactionService, AuthService authService) {
        this.interactionService = interactionService;
        this.authService = authService;
    }

    @PostMapping("/like/add/{targetType}/{targetId}")
    public ResponseEntity<?> addLike(@PathVariable ETargetType targetType, @PathVariable Long targetId) {
        try {
            authService.getCurrentUserId();
            return interactionService.addLike(targetType, targetId);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/like/remove/{targetType}/{targetId}")
    public ResponseEntity<?> removeLike(@PathVariable ETargetType targetType, @PathVariable Long targetId) {
        try {
            authService.getCurrentUserId();
            return interactionService.removeLike(targetType, targetId);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/follow/toggle/{targetUserId}")
    public ResponseEntity<EFollowStatus> toggleFollowRequest(@PathVariable Long targetUserId) {
        try {
            authService.getCurrentUserId();
            return interactionService.toggleFollowRequest(targetUserId);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/follow/status/{targetUserId}")
    public ResponseEntity<EFollowStatus> getFollowStatus(@PathVariable Long targetUserId) {
        try {
            authService.getCurrentUserId();
            return interactionService.getFollowStatus(targetUserId);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/follow/approve/{followerId}")
    public ResponseEntity<?> approveFollow(@PathVariable Long followerId) {
        try {
            authService.getCurrentUserId();
            return interactionService.approveFollow(followerId);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/favorite/add/{targetType}/{targetId}")
    public ResponseEntity<NotificationSimpleDTO> addFavorite(@PathVariable ETargetType targetType,
                                                             @PathVariable Long targetId) {
        try {
            authService.getCurrentUserId();
            return interactionService.addFavorite(targetType, targetId);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/favorite/remove/{targetType}/{targetId}")
    public ResponseEntity<NotificationSimpleDTO> removeFavorite(@PathVariable ETargetType targetType,
                                                                @PathVariable Long targetId) {
        try {
            authService.getCurrentUserId();
            return interactionService.removeFavorite(targetType, targetId);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/byType")
    public ResponseEntity<List<FavoriteItemDTO>> getFavoritesByType(@RequestParam("type") String typeString,
                                                                    @RequestParam(value = "search", required = false, defaultValue = "") String search) {
        try {
            authService.getCurrentUserId();
            ETargetType targetType = ETargetType.valueOf(typeString.toUpperCase());
            List<FavoriteItemDTO> favorites = interactionService.getFavoritesForUserByType(
                    authService.getCurrentUserId(),
                    targetType,
                    search
            );
            return new ResponseEntity<>(favorites, HttpStatus.OK);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
