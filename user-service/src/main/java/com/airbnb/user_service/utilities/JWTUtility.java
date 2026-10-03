package com.airbnb.user_service.utilities;

import com.airbnb.user_service.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Component
public class JWTUtility {

    private final SecretKey secretKey;
    private final long accessTokenExpirySeconds;
    private final long refreshTokenExpirySeconds;

    public JWTUtility(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.access-token-expiry}") long accessTokenExpirySeconds,
            @Value("${app.jwt.refresh-token-expiry}") long refreshTokenExpirySeconds
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.accessTokenExpirySeconds = accessTokenExpirySeconds;
        this.refreshTokenExpirySeconds = refreshTokenExpirySeconds;
    }

    public String generateAccessToken(User user) {
        return buildToken(user, "access", this.accessTokenExpirySeconds);
    }

    public String generateRefreshToken(User user) {
        return buildToken(user, "refresh", this.refreshTokenExpirySeconds);
    }

    private String buildToken(User user, String type, long expirySeconds) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(String.valueOf(user.getId()))
                .claim("email", user.getEmail())
                .claim("name", user.getName())
                .claim("provider", user.getProvider())
                .claim("type", type)
                .signWith(this.secretKey)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(expirySeconds)))
                .compact();
    }

    public Claims parseRefreshToken(String refreshToken) {
        Claims claims = parseToken(refreshToken);
        if (!"refresh".equals(claims.get("type", String.class))) {
            throw new IllegalArgumentException("Token is not refresh token");
        }
        return claims;
    }

    private Claims parseToken(String refreshToken) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(refreshToken)
                    .getPayload();
        } catch (JwtException | IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid or expired token", ex);
        }
    }
}
