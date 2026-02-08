package com.example.tunehub.service;

import com.example.tunehub.model.EDifficultyLevel;
import com.example.tunehub.model.EScale;
import com.example.tunehub.model.SheetMusic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SheetMusicRepository extends JpaRepository<SheetMusic, Long> {
    List<SheetMusic> findAllByUserId(Long id);

    SheetMusic findSheetMusicById(Long id);

    List<SheetMusic> findAllSheetMusicByUser_Id(Long id);

    List<SheetMusic> findAllByTitleContainingIgnoreCase(String title);

    List<SheetMusic> findAllTop5ByTitleContainingIgnoreCase(String title);

    List<SheetMusic> findAllByCategories_Id(Long categoryId);

    List<SheetMusic> findAllSheetMusicByScale(EScale scale);

    List<SheetMusic> findAllSheetMusicByLevel(EDifficultyLevel level);

    void deleteAllByUserId(Long id);

    @Modifying
    @Query("UPDATE SheetMusic sm SET sm.likes = :count WHERE sm.id = :id")
    @Transactional
    void updateLikeCount(@Param("id") Long sheetMusicId, @Param("count") int count);

    @Modifying
    @Query("UPDATE SheetMusic sm SET sm.hearts = :count WHERE sm.id = :id")
    @Transactional
    void updateFavoriteCount(@Param("id") Long sheetMusicId, @Param("count") int count);

    @Query("SELECT COALESCE(SUM(s.likes), 0) FROM SheetMusic s WHERE s.user.id = :userId")
    long sumSheetLikes(@Param("userId") Long userId);

    @Query("SELECT COALESCE(SUM(s.hearts), 0) FROM SheetMusic s WHERE s.user.id = :userId")
    long sumSheetHearts(@Param("userId") Long userId);
}
