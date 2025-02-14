package com.aashish.hospital_management_system.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class JWTService {

    private static final String SECRET_KEY_STRING = "hDk8ZpL3qV9yZ4tX2wQsJ9fJb4L7eQxA";
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET_KEY_STRING.getBytes());

    private static final long EXPIRATION_TIME = 1000 * 60 * 30L; // 30 minutes

    public String generateToken(String username, List<String> permissions, String email) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("permissions", permissions);
        claims.put("email", email);

        return Jwts.builder()
                .subject(username)
                .claims(claims)
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plus(Duration.ofMillis(EXPIRATION_TIME))))
                .signWith(SECRET_KEY)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return extractClaims(token).getSubject();
    }

    @SuppressWarnings("unchecked")
    public List<String> getPermissionsFromToken(String token) {
        return (List<String>) extractClaims(token).get("permissions");
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = extractClaims(token);
            if (claims.getExpiration().before(new Date())) {
                log.warn("JWT token is expired.");
                return false;
            }
            return true;
        } catch (Exception e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
