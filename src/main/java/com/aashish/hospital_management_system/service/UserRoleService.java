package com.aashish.hospital_management_system.service;

import com.aashish.hospital_management_system.configuration.exception.NotFoundException;
import com.aashish.hospital_management_system.entity.Role;
import com.aashish.hospital_management_system.entity.User;
import com.aashish.hospital_management_system.entity.UserRole;
import com.aashish.hospital_management_system.repository.RoleRepository;
import com.aashish.hospital_management_system.repository.UserRepository;
import com.aashish.hospital_management_system.repository.UserRoleRepository;
import com.aashish.hospital_management_system.service.dto.UserRoleDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserRoleService {

    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserRoleService(UserRoleRepository userRoleRepository, UserRepository userRepository, RoleRepository roleRepository) {
        this.userRoleRepository = userRoleRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    // Add a new user-role mapping
    public String addUserRole(UserRoleDTO dto) {
        Optional<User> user = userRepository.findById(dto.getUserId());
        Optional<Role> role = roleRepository.findById(dto.getRoleId());

        if (user.isPresent() && role.isPresent()) {
            UserRole userRole = new UserRole();
            userRole.setUser(user.get());
            userRole.setRole(role.get());
            userRoleRepository.save(userRole);

            return "User-Role mapping added successfully";
        } else {
            throw new NotFoundException("User or Role not found");
        }
    }

    // Get all user-role mappings
    public List<UserRoleDTO> getAllUserRoles() {
        return userRoleRepository.findAll().stream().map(userRole -> {
            UserRoleDTO dto = new UserRoleDTO();
            dto.setUserId(userRole.getUser().getId());
            dto.setRoleId(userRole.getRole().getId());
            return dto;
        }).collect(Collectors.toList());
    }

    // Delete a user-role mapping
    public String deleteUserRole(Integer userId, Set<Integer> roleIds) {
        if (roleIds.isEmpty()) {
            throw new IllegalArgumentException("At least one role ID is required");
        }
        List<Role> allRolesByUserId = roleRepository.findAllById(roleIds);
        Set<Integer> existingRoleIds = allRolesByUserId.stream().map(Role::getId).collect(Collectors.toSet());
        if (!existingRoleIds.containsAll(roleIds)) {
            existingRoleIds.forEach(roleIds::remove);
            throw new NotFoundException("Role with Role ID " + roleIds + " not found");
        }
        userRepository.findById(userId).orElseThrow(()
                -> new NotFoundException("User not found"));
        roleRepository.findAllById(roleIds);

        userRoleRepository.deleteByUserIdAndRoleIdIn(userId, roleIds);
        return "User-Role mappings deleted successfully";
    }
}
