package com.aashish.hospital_management_system.service;

import com.aashish.hospital_management_system.configuration.exception.BadRequestException;
import com.aashish.hospital_management_system.configuration.exception.NotFoundException;
import com.aashish.hospital_management_system.configuration.exception.UnauthorizedException;
import com.aashish.hospital_management_system.entity.Permission;
import com.aashish.hospital_management_system.entity.User;
import com.aashish.hospital_management_system.repository.UserRepository;
import com.aashish.hospital_management_system.service.dto.request_dto.LoginDTO;
import com.aashish.hospital_management_system.service.dto.response_dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;

    @Transactional
    public User registerUser(User user) {
        if (user == null || isNullOrEmpty(user.getUsername()) || isNullOrEmpty(user.getPassword())) {
            throw new BadRequestException("Username and password are required");
        }
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new BadRequestException("Username already taken");
        }
        // Encode the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    /**
     * Authenticate a user and generate a JWT token.
     */
    public String loginUser(LoginDTO loginDTO) {
        // Validate input
        if (isNullOrEmpty(loginDTO.getUsername()) || isNullOrEmpty(loginDTO.getPassword())) {
            throw new BadRequestException("Username and password are required");
        }

        // Retrieve user by username
        User user = userRepository.findByUsername(loginDTO.getUsername())
                .orElseThrow(() -> new UnauthorizedException("Invalid username or password"));

        // Validate credentials
        validateUserCredentials(loginDTO.getPassword(), user.getPassword());

        // Extract roles and permissions
        List<String> permissions = user.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream()) // Flatten permissions from all roles
                .map(Permission::getName)
                .toList();

        // Generate and return JWT token
        return jwtService.generateToken(createAuthentication(user.getUsername(), permissions));
    }

    /**
     * Helper method to create an Authentication object for the user.
     */
    private Authentication createAuthentication(String username, List<String> permissions) {
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(username)
                .password("") // Password is not needed here since authentication is already validated
                .authorities(permissions.stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList())
                .build();
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    /**
     * Get a user by ID.
     */
    public UserResponse getUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return new UserResponse(user);
    }

    /**
     * Get all users.
     */
    public List<UserResponse> getAllUsers() {
        List<User> all = userRepository.findAll();
        List<UserResponse> userResponse = new ArrayList<>();
        all.forEach(user -> userResponse.add(new UserResponse(user)));
        return userResponse;
    }

    /**
     * Validate user credentials.
     */
    private void validateUserCredentials(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new UnauthorizedException("Invalid username or password");
        }
    }

    /**
     * Utility method to check if a string is null or empty.
     */
    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}