package com.example.tunehub.mapper;

import com.example.tunehub.dto.SheetMusicCategoryResponseDTO;
import com.example.tunehub.model.SheetMusicCategory;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SheetMusicCategoryMapper {

    SheetMusicCategoryResponseDTO sheetMusicCategoryToSheetMusicCategoryDTO(SheetMusicCategory sheetMusicCategoryDTO);

    List<SheetMusicCategoryResponseDTO> sheetMusicCategoryListToSheetMusicCategoryDTOList(List<SheetMusicCategory> sheetMusicCategory);

    @Named("mapCategoryListWithoutCreate")
    @IterableMapping(qualifiedByName = "mapCategory")
    List<SheetMusicCategory> mapInstrumentListWithoutCreate(List<SheetMusicCategoryResponseDTO> list);

    @Named("mapCategory")
    SheetMusicCategory mapCategory(SheetMusicCategoryResponseDTO c);

}
