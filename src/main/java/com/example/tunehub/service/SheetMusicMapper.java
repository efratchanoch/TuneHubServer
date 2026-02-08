package com.example.tunehub.service;

import com.example.tunehub.dto.SheetMusicResponseDTO;
import com.example.tunehub.dto.SheetMusicSearchDTO;
import com.example.tunehub.dto.SheetMusicUploadDTO;
import com.example.tunehub.model.SheetMusic;
import org.mapstruct.Context;
import org.mapstruct.Mapper;

import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {InstrumentMapper.class, SheetMusicCategoryMapper.class, UsersMapper.class})
public interface SheetMusicMapper {
    @Mapping(target = "title", source = "title")
    @Mapping(
            target = "imageCoverName",
            expression = "java(com.example.tunehub.service.FileUtils.imageToBase64(s.getImageCoverName()))")
    @Mapping(
            target = "filePath",
            expression = "java(com.example.tunehub.service.FileUtils.documentToBase64(s.getFileName()))")
    @Mapping(
            target = "liked",
            expression = "java(likeRepo.existsByUserIdAndTargetTypeAndTargetId(currentUserId,  com.example.tunehub.model.ETargetType.SHEET_MUSIC, s.getId()))")
    @Mapping(
            target = "favorite",
            expression = "java(favRepo.existsByUserIdAndTargetTypeAndTargetId(currentUserId, com.example.tunehub.model.ETargetType.SHEET_MUSIC, s.getId()))")
    @Mapping(
            target = "likes",
            expression = "java(likeRepo.countByTargetTypeAndTargetId( com.example.tunehub.model.ETargetType.SHEET_MUSIC, s.getId()))")
    @Mapping(
            target = "hearts",
            expression = "java(favRepo.countByTargetTypeAndTargetId( com.example.tunehub.model.ETargetType.SHEET_MUSIC, s.getId()))")
    SheetMusicResponseDTO sheetMusicToSheetMusicResponseDTO(
            SheetMusic s,
            @Context Long currentUserId,
            @Context LikeRepository likeRepo,
            @Context FavoriteRepository favRepo);

    List<SheetMusicResponseDTO> sheetMusicListToSheetMusicResponseDTOlist(
            List<SheetMusic> s,
            @Context Long currentUserId,
            @Context LikeRepository likeRepo,
            @Context FavoriteRepository favRepo);


    @Mapping(target = "dateUploaded", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "hearts", constant = "0")
    @Mapping(target = "likes", constant = "0")
    @Mapping(target = "instruments", ignore = true)
    @Mapping(target = "categories", ignore = true)
    SheetMusic SheetMusicUploadDTOtoSheetMusic(SheetMusicUploadDTO s);

    @Mapping(
            target = "imageCoverName",
            expression = "java(com.example.tunehub.service.FileUtils.imageToBase64(s.getImageCoverName()))")
    @Mapping(target = "userName", source = "user.name")
//    @Mapping(target = "dateUploaded", expression = "java(s.getDateUploaded() != null ? java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(s.getDateUploaded()) : null)")
    SheetMusicSearchDTO sheetMusicToSheetMusicSearchDTO(SheetMusic s);

    List<SheetMusicSearchDTO> sheetMusicListToSheetMusicSearchDTOList(List<SheetMusic> s);
}
