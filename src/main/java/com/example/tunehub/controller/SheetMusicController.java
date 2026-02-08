package com.example.tunehub.controller;

import com.example.tunehub.dto.*;
import com.example.tunehub.model.*;
import com.example.tunehub.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sheetMusic")
public class SheetMusicController {
    private final UsersMapper usersMapper;
    private final UsersRepository usersRepository;
    private final SheetMusicRepository sheetMusicRepository;
    private final SheetMusicMapper sheetMusicMapper;
    private final AuthService authService;
    private final InstrumentRepository instrumentRepository;
    private final SheetMusicCategoryRepository categoryRepository;
    private final SheetAnalysisAgentService agentService;
    private final PdfTextExtractorService extractor;
    private final SheetMusicService sheetMusicService;
    private final LikeRepository likeRepository;
    private final FavoriteRepository favoriteRepository;

    @GetMapping("/sheetMusicById/{id}")
    public ResponseEntity<SheetMusicResponseDTO> getSheetMusicById(@PathVariable Long id) {
        try {
            Long currentUserId = authService.getCurrentUserId();
            SheetMusic s = sheetMusicRepository.findSheetMusicById(id);
            if (s == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(sheetMusicMapper.sheetMusicToSheetMusicResponseDTO(s, currentUserId, likeRepository, favoriteRepository), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/sheetsMusic")
    public ResponseEntity<List<SheetMusicResponseDTO>> getSheetsMusic() {
        try {
            Long currentUserId = authService.getCurrentUserId();
            List<SheetMusic> s = sheetMusicRepository.findAll();
            if (s.isEmpty()) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            UsersRatingUtils.calculateAndSetSheetMusicStarRating(s);
            return new ResponseEntity<>(sheetMusicMapper.sheetMusicListToSheetMusicResponseDTOlist(s, currentUserId, likeRepository, favoriteRepository), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/sheetsMusicByUserId/{id}")
    public ResponseEntity<List<SheetMusicResponseDTO>> getSheetsMusicByUserId(@PathVariable Long id) {
        try {
            Long currentUserId = authService.getCurrentUserId();
            List<SheetMusic> s = sheetMusicRepository.findAllSheetMusicByUser_Id(id);
            if (s == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(sheetMusicMapper.sheetMusicListToSheetMusicResponseDTOlist(s, currentUserId, likeRepository, favoriteRepository), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/sheetsMusicByTitle/{title}")
    public ResponseEntity<List<SheetMusicResponseDTO>> getSheetsMusicByTitle(@PathVariable String title) {
        try {
            Long currentUserId = authService.getCurrentUserId();
            List<SheetMusic> s = sheetMusicRepository.findAllByTitleContainingIgnoreCase(title);
            if (s == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(sheetMusicMapper.sheetMusicListToSheetMusicResponseDTOlist(s, currentUserId, likeRepository, favoriteRepository), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/sheetsMusicByCategory/{category_id}")
    public ResponseEntity<List<SheetMusicResponseDTO>> getSheetsMusicByCategory(@PathVariable Long category_id) {
        try {
            Long currentUserId = authService.getCurrentUserId();
            List<SheetMusic> s = sheetMusicRepository.findAllByCategories_Id(category_id);
            if (s == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(sheetMusicMapper.sheetMusicListToSheetMusicResponseDTOlist(s, currentUserId, likeRepository, favoriteRepository), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/sheetsMusicByScale/{scale}")
    public ResponseEntity<List<SheetMusicResponseDTO>> getSheetsMusicByScale(@PathVariable EScale scale) {
        try {
            Long currentUserId = authService.getCurrentUserId();
            List<SheetMusic> s = sheetMusicRepository.findAllSheetMusicByScale(scale);
            if (s == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(sheetMusicMapper.sheetMusicListToSheetMusicResponseDTOlist(s, currentUserId, likeRepository, favoriteRepository), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/sheetsMusicByLevel/{level}")
    public ResponseEntity<List<SheetMusicResponseDTO>> getSheetsMusicByLevel(@PathVariable EDifficultyLevel level) {
        try {
            Long currentUserId = authService.getCurrentUserId();
            List<SheetMusic> s = sheetMusicRepository.findAllSheetMusicByLevel(level);
            if (s == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(sheetMusicMapper.sheetMusicListToSheetMusicResponseDTOlist(s, currentUserId, likeRepository, favoriteRepository), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/sheetsMusicByUserId/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN' ,'ROLE_SUPER_ADMIN')")
    public ResponseEntity<Void> deleteSheetsMusicByUserId(@PathVariable Long id) {
        try {
            List<SheetMusic> s = sheetMusicRepository.findAllByUserId(id);
            if (s == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            sheetMusicRepository.deleteAllByUserId(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/uploadSheetMusic", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SheetMusicResponseDTO> uploadSheetMusic(
            @Valid @RequestPart("file") MultipartFile file,
            @RequestPart(name = "image", required = false) MultipartFile image,
            @RequestPart("data") SheetMusicUploadDTO dto) {
        try {
            SheetMusicResponseDTO response = sheetMusicService.upload(dto, file, image);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/documents/{docPath}")
    public ResponseEntity<Resource> getDocument(@PathVariable String docPath) throws IOException {
        InputStreamResource resource = FileUtils.getDocument(docPath);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + docPath + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }

    @PostMapping(value = "/analyzePDF", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SheetMusicFinalResponseAIDTO> analyzeSheetMusicPDF(@RequestPart("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            byte[] pdfBytes = file.getBytes();
            SheetMusicResponseAI aiResponse = agentService.analyzePdfBytes(pdfBytes);

            List<InstrumentResponseDTO> finalInstrumentsDTO = new ArrayList<>();
            List<Instrument> instrumentsForSheet = new ArrayList<>();
            for (String instrumentName : aiResponse.instruments()) {
                Instrument inst = instrumentRepository.findByName(instrumentName);
                if (inst != null) {
                    instrumentsForSheet.add(inst);
                    finalInstrumentsDTO.add(new InstrumentResponseDTO(inst.getId(), inst.getName()));
                }
            }

            List<SheetMusicCategoryResponseDTO> finalCategoriesDTO = new ArrayList<>();
            List<SheetMusicCategory> categoriesForSheet = new ArrayList<>();
            if (aiResponse.suggestedCategories() != null) {
                for (String categoryName : aiResponse.suggestedCategories()) {
                    SheetMusicCategory cat = categoryRepository.findByName(categoryName);
                    if (cat == null) {
                        cat = new SheetMusicCategory();
                        cat.setName(categoryName);
                        cat = categoryRepository.save(cat);
                    }
                    categoriesForSheet.add(cat);
                    finalCategoriesDTO.add(new SheetMusicCategoryResponseDTO(cat.getId(), cat.getName()));
                }
            }

            EScale scaleEnum = null;
            if (aiResponse.scale() != null) {
                for (EScale s : EScale.values()) {
                    if (s.name().equalsIgnoreCase(aiResponse.scale())) {
                        scaleEnum = s;
                        break;
                    }
                }
            }

            EDifficultyLevel levelEnum = null;
            if (aiResponse.difficulty() != null) {
                for (EDifficultyLevel lvl : EDifficultyLevel.values()) {
                    if (lvl.name().equalsIgnoreCase(aiResponse.difficulty())) {
                        levelEnum = lvl;
                        break;
                    }
                }
            }

            SheetMusicFinalResponseAIDTO response = new SheetMusicFinalResponseAIDTO(
                    aiResponse.title(),
                    aiResponse.scale(),
                    finalInstrumentsDTO,
                    aiResponse.difficulty(),
                    finalCategoriesDTO,
                    aiResponse.composer(),
                    aiResponse.lyricist()
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
