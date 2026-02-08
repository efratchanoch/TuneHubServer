package com.example.tunehub.service;

import com.example.tunehub.dto.RoleDTO;
import com.example.tunehub.model.Role;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleDTO roleToRoleDTO(Role role);

    Set<RoleDTO> roleSetToRoleDTOSet(Set<Role> set);
}
