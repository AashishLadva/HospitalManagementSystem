package com.aashish.hospital_management_system.service;

import com.aashish.hospital_management_system.configuration.exception.BadRequestException;
import com.aashish.hospital_management_system.configuration.exception.NotFoundException;
import com.aashish.hospital_management_system.constants.ExceptionCommonMessages;
import com.aashish.hospital_management_system.entity.Permission;
import com.aashish.hospital_management_system.entity.Role;
import com.aashish.hospital_management_system.repository.PermissionRepository;
import com.aashish.hospital_management_system.repository.RoleRepository;
import com.aashish.hospital_management_system.service.dto.response.PaginatedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    // Create a new role
    @Transactional
    public String createRole(Role role) {
        if (role == null || role.getName() == null || role.getName().trim().isEmpty()) {
            throw new BadRequestException("Role name cannot be null or empty");
        }
        if (roleRepository.existsByName(role.getName())) {
            throw new BadRequestException("Role with the same name already exists");
        }
        roleRepository.save(role);

        return "Role created successfully";
    }

    // Get all roles
    @Transactional(readOnly = true)
    public PaginatedResponse<Role> getAllRoles(Pageable pageable) {
        Page<Role> rolePage = roleRepository.findAll(pageable); // Fetch paginated data

        // Convert Page<Role> to PaginatedResponse<Role>
        return PaginatedResponse.fromPage(rolePage);
    }

    // Get a role by ID
    @Transactional(readOnly = true)
    public Role getRoleById(Integer id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionCommonMessages.ROLE_NOT_FOUND));
    }

    // Update a role
    @Transactional
    public String updateRole(Integer id, Role updatedRole) {
        if (updatedRole == null || updatedRole.getName() == null || updatedRole.getName().trim().isEmpty()) {
            throw new BadRequestException("Role name cannot be null or empty");
        }
        Role existingRole = roleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionCommonMessages.ROLE_NOT_FOUND));

        existingRole.setName(updatedRole.getName());
        roleRepository.save(existingRole);

        return "Role updated successfully.";
    }

    // Delete a role by ID
    @Transactional
    public String deleteRole(Integer id) {
        if (!roleRepository.existsById(id)) {
            throw new NotFoundException(ExceptionCommonMessages.ROLE_NOT_FOUND);
        }
        roleRepository.deleteById(id);
        return "Role deleted successfully";
    }

    // Add permissions to a role
    @Transactional
    public Role addPermissionsToRole(Integer roleId, List<Integer> permissionIds) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new NotFoundException(ExceptionCommonMessages.ROLE_NOT_FOUND));

        Set<Permission> permissionsToAdd = new HashSet<>(permissionRepository.findAllById(permissionIds));
        if (permissionsToAdd.isEmpty()) {
            throw new NotFoundException("No valid permissions found for the given IDs");
        }

        role.getPermissions().addAll(permissionsToAdd);
        return roleRepository.save(role);
    }

    // Remove permissions from a role
    @Transactional
    public Role removePermissionsFromRole(Integer roleId, List<Integer> permissionIds) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new NotFoundException(ExceptionCommonMessages.ROLE_NOT_FOUND));

        Set<Permission> permissionsToRemove = new HashSet<>(permissionRepository.findAllById(permissionIds));
        if (permissionsToRemove.isEmpty()) {
            throw new NotFoundException("No valid permissions found for the given IDs");
        }

        role.getPermissions().removeAll(permissionsToRemove);
        return roleRepository.save(role);
    }
}
