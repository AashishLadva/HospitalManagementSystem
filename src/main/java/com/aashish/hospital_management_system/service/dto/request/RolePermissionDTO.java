package com.aashish.hospital_management_system.service.dto.request;

import com.aashish.hospital_management_system.entity.RolePermission;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RolePermissionDTO {
    private Integer roleId;
    private Integer permissionId;

    public RolePermissionDTO(RolePermission rolePermission) {
        this.roleId = rolePermission.getRole().getId();
        this.permissionId = rolePermission.getPermission().getId();
    }
}
