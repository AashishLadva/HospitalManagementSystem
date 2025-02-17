package com.aashish.hospital_management_system.service;

import com.aashish.hospital_management_system.configuration.exception.BadRequestException;
import com.aashish.hospital_management_system.configuration.exception.NotFoundException;
import com.aashish.hospital_management_system.constants.ExceptionCommonMessages;
import com.aashish.hospital_management_system.entity.Permission;
import com.aashish.hospital_management_system.repository.PermissionRepository;
import com.aashish.hospital_management_system.service.dto.request.RolePermissionDTO;
import com.aashish.hospital_management_system.service.dto.response.PaginatedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final PermissionRepository permissionRepository;
    private final RolePermissionService rolePermissionService;

    // Create a new permission
    @Transactional
    public String createPermission(Permission permission) {
        if (permission == null || permission.getName() == null || permission.getName().trim().isEmpty()) {
            throw new BadRequestException("Permission name cannot be null or empty");
        }
        if (permissionRepository.existsByName(permission.getName())) {
            throw new BadRequestException("Permission with the same name already exists");
        }

        // Save permission first
        permissionRepository.saveAndFlush(permission); // Ensures the entity is saved before querying

        // Fetch the newly saved permission
        Permission savedPermission = permissionRepository.findByName(permission.getName())
                .orElseThrow(() -> new NotFoundException("Permission not found"));

        // Assign to Super Admin (Role ID 1)
        RolePermissionDTO dto = new RolePermissionDTO(1, savedPermission.getId());
        rolePermissionService.addRolePermission(dto);

        return "Permission created successfully";
    }


    // Get all permissions
    @Transactional(readOnly = true)
    public PaginatedResponse<Permission> getAllPermissions(Pageable pageable) {
        Page<Permission> permissionPage = permissionRepository.findAll(pageable); // Fetch paginated data

        // Convert Page<Permission> to PaginatedResponse<Permission>
        return PaginatedResponse.fromPage(permissionPage);
    }

    // Get a permission by ID
    @Transactional(readOnly = true)
    public Permission getPermissionById(Integer id) {
        return permissionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionCommonMessages.PERMISSION_NOT_FOUND));
    }

    // Update a permission
    @Transactional
    public String updatePermission(Integer id, Permission updatedPermission) {
        if (updatedPermission == null || updatedPermission.getName() == null || updatedPermission.getName().trim().isEmpty()) {
            throw new BadRequestException("Permission name cannot be null or empty");
        }
        Permission existingPermission = permissionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionCommonMessages.PERMISSION_NOT_FOUND));

        existingPermission.setName(updatedPermission.getName());
        permissionRepository.save(existingPermission);

        return "Permission updated successfully.";
    }

    // Delete a permission by ID
    @Transactional
    public String deletePermission(Integer id) {
        if (!permissionRepository.existsById(id)) {
            throw new NotFoundException(ExceptionCommonMessages.PERMISSION_NOT_FOUND);
        }

        permissionRepository.deleteById(id);
        return "Permission deleted successfully";
    }
}
