package com.example.tunehub.service;

import com.example.tunehub.dto.UsersProfileCompleteDTO;
import com.example.tunehub.model.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {InstrumentMapper.class, SheetMusicMapper.class, PostMapper.class, TeacherMapper.class})
public interface UsersProfileCompleteMapper {
    @Mapping(target = "ownProfile", ignore = true)
    @Mapping(target = "canBeMyStudent", ignore = true)
    @Mapping(target = "myStudent", ignore = true)
    @Mapping(target = "canEditRoles", ignore = true)
    @Mapping(target = "canDelete", ignore = true)
    @Mapping(target = "teacherDetails", source = "teacher")
    @Mapping(target = "teacher", source = "teacher.user")
    UsersProfileCompleteDTO toDto(Users user);
}