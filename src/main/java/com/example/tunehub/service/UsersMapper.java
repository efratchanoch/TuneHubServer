package com.example.tunehub.service;

import com.example.tunehub.dto.*;
import com.example.tunehub.model.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;


@Mapper(componentModel = "spring" , uses = {RoleMapper.class})
public  interface  UsersMapper {

    @Mapping(
            target = "imageProfilePath",
            expression = "java(com.example.tunehub.service.FileUtils.imageToBase64(u.getImageProfilePath()))")
    UsersProfileDTO usersToUsersProfileDTO(Users u);

    List<UsersProfileDTO> usersListToUsersProfileDTOList(List<Users> u);

    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "isActive", expression = "java(true)")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "imageProfilePath", source = "imageProfilePath")
    Users usersSignUpDTOtoUsers(UsersSignUpDTO u);

    @Mapping(
            target = "imageProfilePath",
            expression = "java(com.example.tunehub.service.FileUtils.imageToBase64(u.getImageProfilePath()))")
    @Mapping(target = "teacher", source = "u.teacherDetails")
    UsersProfileFullDTO usersToUsersProfileFullDTO(Users u);

    @Mapping(target = "profile", source = "u")
    @Mapping(target = "isActive", source = "u.active")
//    @Mapping(target = "teacher", source = "u.teacherDetails")
    UsersMusiciansDTO usersToUsersMusiciansDTO(Users u);

    List<UsersMusiciansDTO> usersToUsersMusiciansDTO(List<Users> u);

    @Mapping(target = "profile", source = "u")
    UsersSearchDTO usersToUsersSearchDTO(Users u);

    List<UsersSearchDTO> usersListToUsersSearchDTOList(List<Users> u);

    @Mapping(target = "teacher", source = "teacher.user")
    @Mapping(target = "createdAt", expression = "java(user.getCreatedAt() != null ? java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(user.getCreatedAt()) : null)")
    @Mapping(target = "editedIn", expression = "java(user.getEditedIn() != null ? java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(user.getEditedIn()) : null)")
    UsersProfileCompleteDTO usersToUsersProfileCompleteDTO(Users user);
}
