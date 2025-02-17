package com.aashish.hospital_management_system.controller;

import com.aashish.hospital_management_system.constants.UserPermissions;
import com.aashish.hospital_management_system.entity.User;
import com.aashish.hospital_management_system.service.UserService;
import com.aashish.hospital_management_system.service.dto.request.LoginDTO;
import com.aashish.hospital_management_system.service.dto.response.PaginatedResponse;
import com.aashish.hospital_management_system.service.dto.response.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Register a new user (Public access)
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        User registeredUser = userService.registerUser(user);
        return ResponseEntity.ok(registeredUser);
    }

    // Login a user (Public access)
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginDTO loginDTO) {
        String response = userService.loginUser(loginDTO);
        return ResponseEntity.ok(response);
    }

    // Get a user by ID (Accessible to users with 'view_user' permission)
    @GetMapping("/{id}/getUser")
    @PreAuthorize("hasAuthority('" + UserPermissions.READ_USER + "') or hasAuthority('" + UserPermissions.READ_ALL_USERS + "')")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    // Get all users (Accessible to users with 'view_all_users' permission)
    @GetMapping("/getAllUsers")
    @PreAuthorize("hasAuthority('" + UserPermissions.READ_ALL_USERS + "')")
    public ResponseEntity<PaginatedResponse<UserResponse>> getAllUsers(
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(userService.getAllUsers(pageable));
    }
}