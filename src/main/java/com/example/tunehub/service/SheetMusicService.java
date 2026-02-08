package com.example.tunehub.service;

import com.example.tunehub.dto.SheetMusicResponseDTO;
import com.example.tunehub.dto.SheetMusicUploadDTO;
import com.example.tunehub.model.Instrument;
import com.example.tunehub.model.SheetMusic;
import com.example.tunehub.model.SheetMusicCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SheetMusicService {

    private final SheetMusicRepository sheetMusicRepository;
    private final InstrumentRepository instrumentRepository;
    private final SheetMusicCategoryRepository categoryRepository;
    private final SheetMusicMapper sheetMusicMapper;
    private final AuthService authService;
    private final LikeRepository likeRepository;
    private final FavoriteRepository favoriteRepository;

    public SheetMusicResponseDTO upload(SheetMusicUploadDTO dto, MultipartFile file, MultipartFile image) throws Exception {

        SheetMusic s = sheetMusicMapper.SheetMusicUploadDTOtoSheetMusic(dto);
        s.setUser(authService.getCurrentUser());

        String uniqueFileName = null;
        if (file != null && !file.isEmpty()) {
            uniqueFileName = FileUtils.generateUniqueFileName(file);
            s.setFileName(uniqueFileName);
            s.setPages(FileUtils.getPDFPageCount(file.getBytes()));
        }

        String uniqueImageCoverName = null;
        if (image != null && !image.isEmpty()) {
            uniqueImageCoverName = FileUtils.generateUniqueFileName(image);
            s.setImageCoverName(uniqueImageCoverName);
        }

        // instruments
        List<Instrument> instruments = dto.instruments().stream()
                .map(i -> instrumentRepository.findById(i.id())
                        .orElseThrow(() -> new RuntimeException("Instrument not found: id=" + i.id())))
                .toList();
        s.setInstruments(instruments);

        // categories
        List<SheetMusicCategory> categories = dto.categories().stream()
                .map(c -> categoryRepository.findById(c.id())
                        .orElseThrow(() -> new RuntimeException("Category not found: id=" + c.id())))
                .toList();
        s.setCategories(categories);

        sheetMusicRepository.save(s);

        // Upload ONLY if exists
        if (file != null && !file.isEmpty()) {
            FileUtils.uploadDocument(file, uniqueFileName);
        }

        if (image != null && !image.isEmpty()) {
            FileUtils.uploadImage(image, uniqueImageCoverName);
        }

        return sheetMusicMapper.sheetMusicToSheetMusicResponseDTO(
                s, authService.getCurrentUserId(), likeRepository, favoriteRepository);
    }
}
