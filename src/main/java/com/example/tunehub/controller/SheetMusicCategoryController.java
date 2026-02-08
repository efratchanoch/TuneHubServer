package com.example.tunehub.controller;

import com.example.tunehub.dto.SheetMusicCategoryResponseDTO;
import com.example.tunehub.model.SheetMusicCategory;
import com.example.tunehub.service.SheetMusicCategoryMapper;
import com.example.tunehub.service.SheetMusicCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sheetMusicCategory")
public class SheetMusicCategoryController {
    private final SheetMusicCategoryRepository sheetMusicCategoryRepository;
    private final SheetMusicCategoryMapper sheetMusicCategoryMapper;

    @Autowired
    public SheetMusicCategoryController(SheetMusicCategoryRepository sheetMusicCategoryRepository, SheetMusicCategoryMapper sheetMusicCategoryMapper) {
        this.sheetMusicCategoryRepository = sheetMusicCategoryRepository;
        this.sheetMusicCategoryMapper = sheetMusicCategoryMapper;
    }

    @GetMapping("/sheetMusicCategoryById/{id}")
    public ResponseEntity<SheetMusicCategoryResponseDTO> getSheetMusicCategoryById(@PathVariable Long id) {
        try {
            SheetMusicCategory sc = sheetMusicCategoryRepository.findSheetMusicCategoryById(id);
            if (sc == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(sheetMusicCategoryMapper.sheetMusicCategoryToSheetMusicCategoryDTO(sc), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/categoriesByName/{name}")
    public ResponseEntity<List<SheetMusicCategoryResponseDTO>> getCategoriesByName(@PathVariable String name) {
        try {
            List<SheetMusicCategory> sc = sheetMusicCategoryRepository.findAllByNameContainingIgnoreCase(name);
            if (sc == null || sc.isEmpty()) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(sheetMusicCategoryMapper.sheetMusicCategoryListToSheetMusicCategoryDTOList(sc), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/sheetMusicCategories")
    public ResponseEntity<List<SheetMusicCategoryResponseDTO>> getSheetMusicCategories() {
        try {
            List<SheetMusicCategory> sc = sheetMusicCategoryRepository.findAll();
            if (sc == null || sc.isEmpty()) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(sheetMusicCategoryMapper.sheetMusicCategoryListToSheetMusicCategoryDTOList(sc), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN')")
    @DeleteMapping("/sheetMusicCategoryById/{id}")
    public ResponseEntity<Void> deleteSheetMusicCategoryById(@PathVariable Long id) {
        try {
            if (sheetMusicCategoryRepository.existsById(id)) {
                sheetMusicCategoryRepository.deleteById(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
