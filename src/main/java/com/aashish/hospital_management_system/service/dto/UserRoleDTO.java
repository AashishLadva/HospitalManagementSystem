package com.aashish.hospital_management_system.service.dto;

import com.aashish.hospital_management_system.entity.UserRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRoleDTO {
    private Integer userId;
    private Integer roleId;

    public UserRoleDTO(UserRole userRole) {
        this.userId = userRole.getId();
        this.roleId = userRole.getRole().getId();
    }
}
