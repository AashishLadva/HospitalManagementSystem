package com.aashish.hospital_management_system.service;

import com.aashish.hospital_management_system.configuration.exception.BadRequestException;
import com.aashish.hospital_management_system.configuration.exception.NotFoundException;
import com.aashish.hospital_management_system.entity.Permission;
import com.aashish.hospital_management_system.entity.Role;
import com.aashish.hospital_management_system.entity.RolePermission;
import com.aashish.hospital_management_system.repository.PermissionRepository;
import com.aashish.hospital_management_system.repository.RolePermissionRepository;
import com.aashish.hospital_management_system.repository.RoleRepository;
import com.aashish.hospital_management_system.service.dto.request.RolePermissionDTO;
import com.aashish.hospital_management_system.service.dto.response.PaginatedResponse;
import com.aashish.hospital_management_system.service.dto.response.RolePermissionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RolePermissionService {

    private final RolePermissionRepository rolePermissionRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public RolePermissionService(RolePermissionRepository rolePermissionRepository, RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.rolePermissionRepository = rolePermissionRepository;
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    // Add a new role-permission mapping
    @Transactional
    public String addRolePermission(RolePermissionDTO dto) {
        Optional<Role> role = roleRepository.findById(dto.getRoleId());
        Optional<Permission> permission = permissionRepository.findById(dto.getPermissionId());


        if (role.isPresent() && permission.isPresent()) {
            Optional<RolePermission> rolePermissionExist = rolePermissionRepository.findByRoleAndPermission(role.get(), permission.get());
            if (rolePermissionExist.isPresent()) {
                throw new BadRequestException("Role-Permission mapping already exists");
            }
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRole(role.get());
            rolePermission.setPermission(permission.get());
            rolePermissionRepository.save(rolePermission);

            return "Role-Permission mapping added successfully";
        } else {
            throw new NotFoundException("Role or Permission not found");
        }
    }

    // Get all role-permission mappings
    @Transactional(readOnly = true)
    public PaginatedResponse<RolePermissionDTO> getAllRolePermissions(Pageable pageable) {
        Page<RolePermission> rolePermissionPage = rolePermissionRepository.findAll(pageable); // Fetch paginated data

        // Convert Page<RolePermission> to PaginatedResponse<RolePermissionDTO>
        Page<RolePermissionDTO> rolePermissionDTOPage = rolePermissionPage.map(RolePermissionDTO::new);

        return PaginatedResponse.fromPage(rolePermissionDTOPage);
    }

    @Transactional
    public PaginatedResponse<RolePermissionResponse> getAllPermissionsByRoleId(Integer id, Pageable pageable) {
        // Fetch paginated permission IDs for the given role ID
        Page<Integer> permissionIdsPage = rolePermissionRepository.findPermissionIdsByRoleId(id, pageable);

        // If no permissions found, throw an exception
        if (permissionIdsPage.isEmpty()) {
            throw new NotFoundException("No permissions found for the given role ID: " + id);
        }

        // Create one RolePermissionResponse for the given role with the current page's permission IDs
        RolePermissionResponse response = new RolePermissionResponse(id, permissionIdsPage.getContent());

        // Build a PaginatedResponse containing a single RolePermissionResponse
        return new PaginatedResponse<>(
                permissionIdsPage.getNumber(),         // current page
                permissionIdsPage.getTotalPages(),     // total pages
                permissionIdsPage.getTotalElements(),  // total elements
                permissionIdsPage.getSize(),           // page size
                permissionIdsPage.isLast(),            // is last page
                List.of(response)                      // wrap the single response in a list
        );
    }


    // Delete a role-permission mapping
    public String deleteRolePermission(Integer id) {
        rolePermissionRepository.findById(id).orElseThrow(()
                -> new NotFoundException("Role-Permission mapping not found"));
        rolePermissionRepository.deleteById(id);
        return "Role-Permission mapping deleted successfully";
    }
}
