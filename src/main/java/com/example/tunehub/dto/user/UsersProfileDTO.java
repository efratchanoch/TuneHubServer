package com.example.tunehub.dto.user;

import com.example.tunehub.dto.common.RoleDTO;
import com.example.tunehub.model.EUserType;

import java.util.Set;

public record UsersProfileDTO(
        String id,
        String name,
        String imageProfilePath,
        Set<RoleDTO> roles,
        Set<EUserType> userTypes
) {}