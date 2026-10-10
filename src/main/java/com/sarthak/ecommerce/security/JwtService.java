package com.sarthak.ecommerce.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {
    private final SecretKey key;
    private final long accessMinutes;
    private final long refreshDays;

    public JwtService(@Value("${app.jwt.secret}") String secret,
                      @Value("${app.jwt.access-minutes:15}") long accessMinutes,
                      @Value("${app.jwt.refresh-days:7}") long refreshDays) {
        if (secret.getBytes(StandardCharsets.UTF_8).length < 32) throw new IllegalArgumentException("app.jwt.secret must be at least 32 bytes");
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessMinutes = accessMinutes;
        this.refreshDays = refreshDays;
    }

    public String accessToken(String email, List<String> roles) {
        Instant now = Instant.now();
        return Jwts.builder().subject(email).claim("roles", roles).claim("type", "access")
            .issuedAt(Date.from(now)).expiration(Date.from(now.plusSeconds(accessMinutes * 60)))
            .signWith(key).compact();
    }
    public String refreshToken(String email) {
        Instant now = Instant.now();
        return Jwts.builder().subject(email).claim("type", "refresh")
            .issuedAt(Date.from(now)).expiration(Date.from(now.plusSeconds(refreshDays * 86400)))
            .signWith(key).compact();
    }
    public Claims parse(String token) { return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload(); }
}
