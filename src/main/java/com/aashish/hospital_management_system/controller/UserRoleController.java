package com.aashish.hospital_management_system.controller;

import com.aashish.hospital_management_system.constants.UserPermissions;
import com.aashish.hospital_management_system.service.UserRoleService;
import com.aashish.hospital_management_system.service.dto.UserRoleDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/user-roles")
public class UserRoleController {

    private final UserRoleService userRoleService;

    public UserRoleController(UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    // Add a user-role mapping
    @PostMapping("/add-UserRole")
    @PreAuthorize("hasAuthority('" + UserPermissions.WRITE_USER_ROLES + "')")
    public ResponseEntity<String> addUserRole(@RequestBody UserRoleDTO dto) {
        return ResponseEntity.ok(userRoleService.addUserRole(dto));
    }

    // Get all user-role mappings
    @GetMapping("getAllUserRoles")
    @PreAuthorize("hasAuthority('" + UserPermissions.READ_ALL_USER_ROLES + "')")
    public ResponseEntity<List<UserRoleDTO>> getAllUserRoles() {
        return ResponseEntity.ok(userRoleService.getAllUserRoles());
    }

    // Delete a user-role mapping
    @DeleteMapping("/{id}/deleteUserRole")
    @PreAuthorize("hasAuthority('" + UserPermissions.WRITE_USER_ROLES + "')")
    public ResponseEntity<String> deleteUserRole(@PathVariable Integer id, @RequestBody Set<Integer> roleIds) {

        return ResponseEntity.ok(userRoleService.deleteUserRole(id, roleIds));
    }
}
