package com.sitix.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.sitix.model.entity.User;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class JwtUtil {
    @Value("${app.sitix.jwt.jwt-secret}")
    private String jwtSecret;
    @Value("${app.sitix.jwt.app-name}")
    private String appName;
    @Value("${app.sitix.jwt.jwt-expired}")
    private long jwtExpired;

    private Algorithm algorithm;

    @PostConstruct
    private void initAlgorithm() {
        this.algorithm = Algorithm.HMAC256(jwtSecret.getBytes(StandardCharsets.UTF_8));

    }

    public String generateToken(User user) {

        return JWT.create()
                .withIssuer(appName)
                .withSubject(user.getId())
                .withExpiresAt(Instant.now().plusSeconds(jwtExpired))
                .withIssuedAt(Instant.now())
                .withClaim("role", user.getRole().getName().name())
                .sign(algorithm);

    }

    public boolean verifyToken(String token) {

        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        return decodedJWT.getIssuer().equals(appName);
    }

    public Map<String, String> getUserInfoByToken(String token) {
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);

        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("userId", decodedJWT.getSubject());
        userInfo.put("role", decodedJWT.getClaim("role").asString());
        return userInfo;

    }
}