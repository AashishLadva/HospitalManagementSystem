package com.aashish.hospital_management_system.controller;

import com.aashish.hospital_management_system.constants.UserPermissions;
import com.aashish.hospital_management_system.entity.Role;
import com.aashish.hospital_management_system.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    // Create a new role
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('" + UserPermissions.WRITE_ROLES + "')")
    public ResponseEntity<String> createRole(@RequestBody Role role) {
        return ResponseEntity.ok(roleService.createRole(role));
    }

    // Get all roles
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('" + UserPermissions.READ_ROLES + "')")
    public ResponseEntity<List<Role>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    // Get a role by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('" + UserPermissions.READ_ROLES + "')")
    public ResponseEntity<Role> getRoleById(@PathVariable Integer id) {
        return ResponseEntity.ok(roleService.getRoleById(id));
    }

    // Update a role
    @PutMapping("/{id}/update")
    @PreAuthorize("hasAuthority('" + UserPermissions.WRITE_ROLES + "')")
    public ResponseEntity<String> updateRole(@PathVariable Integer id, @RequestBody Role updatedRole) {
        return ResponseEntity.ok(roleService.updateRole(id, updatedRole));
    }

    // Delete a role by ID
    @DeleteMapping("/{id}/delete")
    @PreAuthorize("hasAuthority('" + UserPermissions.WRITE_ROLES + "')")
    public ResponseEntity<String> deleteRole(@PathVariable Integer id) {
        return ResponseEntity.ok(roleService.deleteRole(id));
    }
}