package com.example.tunehub.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.tunehub.dto.GlobalSearchResponseDTO;
import com.example.tunehub.service.GeneralSearchService;
import com.example.tunehub.service.AuthService;

@RestController
@RequestMapping("/api/search")
public class GeneralSearchController {

    private final GeneralSearchService generalSearchService;
    private final AuthService authService;

    @Autowired
    public GeneralSearchController(GeneralSearchService generalSearchService, AuthService authService) {
        this.generalSearchService = generalSearchService;
        this.authService = authService;
    }

    @GetMapping("/global/{searchTerm}")
    public ResponseEntity<GlobalSearchResponseDTO> globalSearch(@PathVariable String searchTerm) {
        try {
            Long userId = authService.getCurrentUserId();
            GlobalSearchResponseDTO results = generalSearchService.executeGlobalSearch(searchTerm);

            if (!results.hasResults()) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(results, HttpStatus.OK);

        } catch (IllegalStateException e) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
