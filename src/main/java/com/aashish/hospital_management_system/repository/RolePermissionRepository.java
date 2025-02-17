package com.aashish.hospital_management_system.repository;

import com.aashish.hospital_management_system.entity.Permission;
import com.aashish.hospital_management_system.entity.Role;
import com.aashish.hospital_management_system.entity.RolePermission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, Integer> {
    Optional<RolePermission> findByRoleAndPermission(Role role, Permission permission);

    Page<RolePermission> findAllByRoleId(Integer id, Pageable pageable);

    @Query("SELECT rp.permission.id FROM RolePermission rp WHERE rp.role.id = :roleId")
    Page<Integer> findPermissionIdsByRoleId(@Param("roleId") Integer roleId, Pageable pageable);
}
