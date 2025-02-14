package com.aashish.hospital_management_system.controller;

import com.aashish.hospital_management_system.constants.UserPermissions;
import com.aashish.hospital_management_system.service.RolePermissionService;
import com.aashish.hospital_management_system.service.dto.request.RolePermissionDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/role-permissions")
public class RolePermissionController {

    private final RolePermissionService rolePermissionService;

    public RolePermissionController(RolePermissionService rolePermissionService) {
        this.rolePermissionService = rolePermissionService;
    }

    // Add a role-permission mapping
    @PostMapping("/addRolePermission")
    @PreAuthorize("hasAuthority('" + UserPermissions.WRITE_ROLES_PERMISSIONS + "')")
    public ResponseEntity<String> addRolePermission(@RequestBody RolePermissionDTO dto) {
        return ResponseEntity.ok(rolePermissionService.addRolePermission(dto));
    }

    // Get all role-permission mappings
    @GetMapping("/getAllRolePermissions")
    @PreAuthorize("hasAuthority('" + UserPermissions.READ_ALL_ROLES_PERMISSIONS + "')")
    public ResponseEntity<List<RolePermissionDTO>> getAllRolePermissions() {
        return ResponseEntity.ok(rolePermissionService.getAllRolePermissions());
    }


    // Delete a role-permission mapping
    @DeleteMapping("/{id}/delete-role-permission")
    @PreAuthorize("hasAuthority('" + UserPermissions.WRITE_ROLES_PERMISSIONS + "')")
    public ResponseEntity<String> deleteRolePermission(@PathVariable Integer id) {
        return ResponseEntity.ok(rolePermissionService.deleteRolePermission(id));
    }
}
