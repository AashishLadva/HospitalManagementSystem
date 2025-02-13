package com.aashish.hospital_management_system.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Service
public class JWTService {

    // Secret key for signing the token (must be exactly 256 bits for HS256)
    private static final String SECRET_KEY_STRING = "hDk8ZpL3qV9yZ4tX2wQsJ9fJb4L7eQxA"; // 32-character key
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET_KEY_STRING.getBytes());

    // Token expiration time (30 minutes in milliseconds)
    private static final long EXPIRATION_TIME = 1000 * 60 * 30L; // 30 minutes

    /**
     * Generate JWT token
     */
    public String generateToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // Extract permissions from the user's authorities
        List<String> permissions = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return Jwts.builder()
                .subject(userDetails.getUsername()) // Set the subject (username)
                .claim("permissions", permissions)  // Include permissions in the token
                .issuedAt(new Date())              // Set the issue date
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Set expiration
                .signWith(SECRET_KEY)             // Sign the token with the secret key
                .compact();
    }

    /**
     * Extract username from JWT
     */
    public String getUsernameFromToken(String token) {
        return extractClaims(token).getSubject();
    }

    /**
     * Extract permissions from JWT
     */
    @SuppressWarnings("unchecked")
    public List<String> getPermissionsFromToken(String token) {
        return (List<String>) extractClaims(token).get("permissions");
    }

    /**
     * Validate JWT token
     */
    public boolean validateToken(String token) {
        try {
            extractClaims(token); // Attempt to parse the token
            return true;          // If parsing succeeds, the token is valid
        } catch (Exception e) {
            return false;         // If an exception occurs, the token is invalid
        }
    }

    /**
     * Helper method to extract claims from a token
     */
    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY) // Verify the token with the secret key
                .build()
                .parseSignedClaims(token) // Parse the signed claims
                .getPayload();           // Extract the payload (claims)
    }
}