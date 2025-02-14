package com.aashish.hospital_management_system.service.dto.response;

import com.aashish.hospital_management_system.entity.User;
import lombok.Data;

import java.util.Set;

@Data
public class UserResponse {
    private Integer id;
    private String username;
    private String email;
    private String phone;
    private Set<String> roles; // Only role names

    // Constructor to map from User entity to DTO
    public UserResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.roles = user.getRoleNames(); // Using getRoleNames() method
    }
}
