package com.vitaliy.apigateway.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;


@Service
public class JwtService {

    private final RSAPublicKey publicKey;

    public JwtService(@Value("${jwt.public-key}") Resource publicKeyResource) {
        try {
            this.publicKey = loadPublicKey(publicKeyResource);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load public key", e);
        }
    }

    public boolean isTokenValid(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private RSAPublicKey loadPublicKey(Resource resource) throws Exception {
        String key = new String(resource.getInputStream().readAllBytes())
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");
        byte[] decoded = Base64.getDecoder().decode(key);

        return (RSAPublicKey) KeyFactory.getInstance("RSA")
                .generatePublic(new X509EncodedKeySpec(decoded));
    }

    public Long extractUserId(String token) {
        Number userId = parseClaims(token).get("userId", Number.class);
        return userId != null ? userId.longValue() : null;
    }

    public String extractRole(String token) {
        Object roles = parseClaims(token).get("roles");

        if (roles instanceof java.util.List<?> list && !list.isEmpty()) {
            return list.get(0).toString();
        }

        return null;
    }
    public String extractEmail(String token) {
        return parseClaims(token).getSubject();
    }
}
