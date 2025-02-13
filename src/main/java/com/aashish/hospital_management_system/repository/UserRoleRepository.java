package com.aashish.hospital_management_system.repository;

import com.aashish.hospital_management_system.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {
    void deleteByUserIdAndRoleIdIn(Integer userId, Set<Integer> roleIds);
}
