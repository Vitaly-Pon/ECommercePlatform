package com.vitaliy.authservice.config.security;

import com.vitaliy.authservice.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    public String generateToken(User user) {
        return Jwts.builder()
                .subject(user.getEmail()) // 🟢 Новый синтаксис (без set)
                .claim("userId", user.getId())
                .claim("role", user.getRole().name())
                .issuedAt(new Date()) // 🟢 Новый синтаксис (без set)
                .expiration(new Date(System.currentTimeMillis() + expiration)) // 🟢 Новый синтаксис (без set)
                .signWith(getKey()) // 🟢 Использует SecretKey
                .compact();
    }

    public boolean isTokenValid(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Long extractUserId(String token) {
        Number userId = extractAllClaims(token).get("userId", Number.class);
        return userId != null ? userId.longValue() : null;
    }

    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser() // 🟢 Новый синтаксис для 0.13.0
                .verifyWith(getKey()) // 🟢 Вместо setSigningKey
                .build()
                .parseSignedClaims(token) // 🟢 Вместо parseClaimsJws
                .getPayload(); // 🟢 Вместо getBody
    }

    private SecretKey getKey() { // 🟢 Возвращает SecretKey вместо Key
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}