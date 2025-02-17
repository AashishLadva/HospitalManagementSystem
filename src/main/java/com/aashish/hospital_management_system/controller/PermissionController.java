package com.aashish.hospital_management_system.controller;

import com.aashish.hospital_management_system.constants.UserPermissions;
import com.aashish.hospital_management_system.entity.Permission;
import com.aashish.hospital_management_system.service.PermissionService;
import com.aashish.hospital_management_system.service.dto.response.PaginatedResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/permissions")
public class PermissionController {

    private final PermissionService permissionService;

    @Autowired
    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    // Create a new permission
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('" + UserPermissions.WRITE_PERMISSIONS + "')")
    public ResponseEntity<String> createPermission(@RequestBody Permission permission) {
        return ResponseEntity.ok(permissionService.createPermission(permission));

    }

    // Get all permissions
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('" + UserPermissions.READ_ALL_PERMISSIONS + "')")
    public ResponseEntity<PaginatedResponse<Permission>> getAllPermissions(
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(permissionService.getAllPermissions(pageable));
    }

    // Get a permission by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('" + UserPermissions.READ_PERMISSIONS + "') or hasAuthority('" + UserPermissions.READ_ALL_PERMISSIONS + "')")
    public ResponseEntity<Permission> getPermissionById(@PathVariable Integer id) {
        return ResponseEntity.ok(permissionService.getPermissionById(id));
    }

    // Update a permission
    @PutMapping("/{id}/update")
    @PreAuthorize("hasAuthority('" + UserPermissions.WRITE_PERMISSIONS + "')")
    public ResponseEntity<String> updatePermission(@PathVariable Integer id, @RequestBody Permission updatedPermission) {
        return ResponseEntity.ok(permissionService.updatePermission(id, updatedPermission));
    }

    // Delete a permission by ID
    @DeleteMapping("/{id}/delete")
    @PreAuthorize("hasAuthority('" + UserPermissions.WRITE_PERMISSIONS + "')")
    public ResponseEntity<String> deletePermission(@PathVariable Integer id) {
        return ResponseEntity.ok(permissionService.deletePermission(id));
    }
}