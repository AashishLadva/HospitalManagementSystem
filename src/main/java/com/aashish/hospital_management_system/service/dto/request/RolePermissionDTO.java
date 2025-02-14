package com.aashish.hospital_management_system.service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RolePermissionDTO {
    private Integer roleId;
    private Integer permissionId;
}
