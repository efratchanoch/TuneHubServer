package com.example.tunehub.mapper;

import com.example.tunehub.dto.*;
import com.example.tunehub.model.Teacher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UsersMapper.class, InstrumentMapper.class})
public interface TeacherMapper {

    @Mapping(target = "name", source = "user.name")
    @Mapping(target = "city", source = "user.city")
    @Mapping(target = "country", source = "user.country")
    @Mapping(target = "students", source = "teacher.students")
    TeacherListingDTO toTeacherListingDTO(Teacher teacher);
}
