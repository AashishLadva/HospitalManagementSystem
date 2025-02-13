package com.aashish.hospital_management_system.repository;

import com.aashish.hospital_management_system.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Integer> {
    Optional<Permission> findByName(String name);

    boolean existsByName(String name);
}
