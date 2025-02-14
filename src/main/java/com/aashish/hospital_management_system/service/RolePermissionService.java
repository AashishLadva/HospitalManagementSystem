package com.aashish.hospital_management_system.service;

import com.aashish.hospital_management_system.configuration.exception.NotFoundException;
import com.aashish.hospital_management_system.entity.Permission;
import com.aashish.hospital_management_system.entity.Role;
import com.aashish.hospital_management_system.entity.RolePermission;
import com.aashish.hospital_management_system.repository.PermissionRepository;
import com.aashish.hospital_management_system.repository.RolePermissionRepository;
import com.aashish.hospital_management_system.repository.RoleRepository;
import com.aashish.hospital_management_system.service.dto.request.RolePermissionDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public String addRolePermission(RolePermissionDTO dto) {
        Optional<Role> role = roleRepository.findById(dto.getRoleId());
        Optional<Permission> permission = permissionRepository.findById(dto.getPermissionId());

        if (role.isPresent() && permission.isPresent()) {
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
    public List<RolePermissionDTO> getAllRolePermissions() {
        List<RolePermissionDTO> list = new ArrayList<>();
        for (RolePermission permission : rolePermissionRepository.findAll()) {
            RolePermissionDTO dto = new RolePermissionDTO(permission.getRole().getId(), permission.getPermission().getId());
            list.add(dto);
        }
        return list;
    }


    // Delete a role-permission mapping
    public String deleteRolePermission(Integer id) {
        rolePermissionRepository.findById(id).orElseThrow(()
                -> new NotFoundException("Role-Permission mapping not found"));
        rolePermissionRepository.deleteById(id);
        return "Role-Permission mapping deleted successfully";
    }
}
