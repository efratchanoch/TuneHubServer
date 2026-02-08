package com.example.tunehub.controller;

import com.example.tunehub.dto.RoleDTO;
import com.example.tunehub.model.*;
import com.example.tunehub.service.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/role")
public class RoleController {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final UsersRepository usersRepository;
    private final NotificationRepository notificationRepository;

    @Autowired
    public RoleController(RoleRepository roleRepository, RoleMapper roleMapper, UsersRepository usersRepository, NotificationRepository notificationRepository) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
        this.usersRepository = usersRepository;
        this.notificationRepository = notificationRepository;
    }

    @PutMapping("/admin/{userId}/role")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN')")
    @Transactional
    public ResponseEntity<?> updateRole(@PathVariable Long userId, @RequestBody RoleDTO roleDto) {

        String newRoleString = roleDto.getName();

        if (newRoleString == null || newRoleString.isEmpty()) {
            return ResponseEntity.badRequest().body("The role field is missing in the request body.");
        }

        try {
            Users user = usersRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("UserId  " + userId + "not found."));

            ERole targetRoleEnum;
            try {
                targetRoleEnum = ERole.valueOf(newRoleString);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("role " + newRoleString + " no legal .");
            }

            Role targetRole = roleRepository.findByName(targetRoleEnum)
                    .orElseThrow(() -> new IllegalArgumentException("Role Entity " + newRoleString));

            user.getRoles().clear();
            user.getRoles().add(targetRole);
            user.getUserTypes().add(EUserType.MANAGER);
            usersRepository.save(user);

            String notificationMessage = "Congratulations! You have been successfully promoted to the role: " + newRoleString.replace("ROLE_", "");
            Notification notification = new Notification();
            notification.setUser(user);
            notification.setMessage(notificationMessage);
            notification.setRead(false);
            notificationRepository.save(notification);

            return ResponseEntity.ok().body("Role successfully updated to: " + newRoleString);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error updating role: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Internal server error: " + e.getMessage());
        }
    }


}
