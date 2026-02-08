package com.example.tunehub.service;

import com.example.tunehub.model.SheetMusicCategory;
import com.example.tunehub.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SheetMusicCategoryRepository extends JpaRepository<SheetMusicCategory, Long> {
    SheetMusicCategory findSheetMusicCategoryById(Long id);

    SheetMusicCategory findByName(String name);

    List<SheetMusicCategory> findAllByNameContainingIgnoreCase(String name);

}
