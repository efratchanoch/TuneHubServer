package com.example.tunehub.repository;

import com.example.tunehub.model.SheetMusicCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SheetMusicCategoryRepository extends JpaRepository<SheetMusicCategory, Long> {
    SheetMusicCategory findSheetMusicCategoryById(Long id);

    SheetMusicCategory findByName(String name);

    List<SheetMusicCategory> findAllByNameContainingIgnoreCase(String name);

}
