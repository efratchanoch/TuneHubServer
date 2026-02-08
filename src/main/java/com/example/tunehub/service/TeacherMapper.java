package com.example.tunehub.service;

import com.example.tunehub.dto.*;
import com.example.tunehub.model.Teacher;
import com.example.tunehub.model.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.io.IOException;
import java.util.List;

@Mapper(componentModel = "spring", uses = {UsersMapper.class, InstrumentMapper.class})
public interface TeacherMapper {

    @Mapping(target = "name", source = "user.name")
    @Mapping(target = "city", source = "user.city")
    @Mapping(target = "country", source = "user.country")
    @Mapping(target = "students", source = "teacher.students")
    TeacherListingDTO toTeacherListingDTO(Teacher teacher);
}
