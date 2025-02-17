package com.aashish.hospital_management_system.controller;

import com.aashish.hospital_management_system.constants.UserPermissions;
import com.aashish.hospital_management_system.service.RolePermissionService;
import com.aashish.hospital_management_system.service.dto.request.RolePermissionDTO;
import com.aashish.hospital_management_system.service.dto.response.PaginatedResponse;
import com.aashish.hospital_management_system.service.dto.response.RolePermissionResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<PaginatedResponse<RolePermissionDTO>> getAllRolePermissions(
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(rolePermissionService.getAllRolePermissions(pageable));
    }

    @GetMapping("/by-role/{roleId}")
    @PreAuthorize("hasAuthority('" + UserPermissions.READ_ALL_ROLES_PERMISSIONS + "' ) or hasAuthority('" + UserPermissions.READ_ROLES_PERMISSIONS + "')")
    public ResponseEntity<PaginatedResponse<RolePermissionResponse>> getPermissionsByRoleId(
            @PathVariable("roleId") Integer roleId,
            @PageableDefault(size = 10) Pageable pageable) {

        PaginatedResponse<RolePermissionResponse> response = rolePermissionService.getAllPermissionsByRoleId(roleId, pageable);
        return ResponseEntity.ok(response);
    }


    // Delete a role-permission mapping
    @DeleteMapping("/{id}/delete-role-permission")
    @PreAuthorize("hasAuthority('" + UserPermissions.WRITE_ROLES_PERMISSIONS + "')")
    public ResponseEntity<String> deleteRolePermission(@PathVariable Integer id) {
        return ResponseEntity.ok(rolePermissionService.deleteRolePermission(id));
    }
}
