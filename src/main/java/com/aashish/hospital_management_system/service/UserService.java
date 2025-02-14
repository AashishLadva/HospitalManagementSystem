package com.aashish.hospital_management_system.service;

import com.aashish.hospital_management_system.configuration.exception.BadRequestException;
import com.aashish.hospital_management_system.configuration.exception.NotFoundException;
import com.aashish.hospital_management_system.configuration.exception.UnauthorizedException;
import com.aashish.hospital_management_system.entity.Permission;
import com.aashish.hospital_management_system.entity.User;
import com.aashish.hospital_management_system.repository.UserRepository;
import com.aashish.hospital_management_system.service.dto.request.LoginDTO;
import com.aashish.hospital_management_system.service.dto.response.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;

    /**
     * Register a new user.
     */
    @Transactional
    public User registerUser(User user) {
        if (user == null || isNullOrEmpty(user.getUsername()) || isNullOrEmpty(user.getPassword())) {
            throw new BadRequestException("Username and password are required.");
        }
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new BadRequestException("Username '" + user.getUsername() + "' is already taken.");
        }

        // Encode password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        log.info("User '{}' registered successfully.", user.getUsername());
        return userRepository.save(user);
    }

    /**
     * Authenticate user and generate JWT token.
     */
    @Transactional
    public String loginUser(LoginDTO loginDTO) {
        if (isNullOrEmpty(loginDTO.getUsername()) || isNullOrEmpty(loginDTO.getPassword())) {
            throw new BadRequestException("Username and password are required.");
        }

        // Retrieve user by username
        User user = userRepository.findByUsername(loginDTO.getUsername())
                .orElseThrow(() -> new UnauthorizedException("Invalid username or password."));

        // Validate password
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            log.warn("Failed login attempt for user '{}'.", loginDTO.getUsername());
            throw new UnauthorizedException("Invalid username or password.");
        }

        // Extract roles and permissions
        List<String> permissions = user.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(Permission::getName)
                .toList();

        log.info("User '{}' logged in successfully.", loginDTO.getUsername());

        // Generate and return JWT token
        return jwtService.generateToken(user.getUsername(), permissions, user.getEmail());
    }

    /**
     * Get a user by ID.
     */
    public UserResponse getUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + id));
        return new UserResponse(user);
    }

    /**
     * Get all users.
     */
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserResponse::new)
                .toList();
    }

    /**
     * Utility method to check if a string is null or empty.
     */
    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}
