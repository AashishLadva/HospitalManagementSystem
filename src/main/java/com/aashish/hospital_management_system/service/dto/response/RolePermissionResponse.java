package com.aashish.hospital_management_system.service.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class RolePermissionResponse {
    private Integer roleId;
    private List<Integer> permissionIds; // Change from single Integer to List<Integer>

    public RolePermissionResponse(Integer roleId, List<Integer> permissionIds) {
        this.roleId = roleId;
        this.permissionIds = permissionIds;
    }
}
