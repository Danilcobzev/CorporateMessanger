package com.example.firstSpringProgect.providers;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class JwtConfirmProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtConfirmProvider.class);

    private static final String SECRET_KEY_STRING = "sdfgh";

    private SecretKey secretKey;

    @PostConstruct
    private void initialize() {
        try {
            secretKey = new SecretKeySpec(
                    MessageDigest.getInstance("SHA-256")
                            .digest(SECRET_KEY_STRING.getBytes()),
                    "HmacSHA256"
            );
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("",e);
        }
    }

    public String generateToken(Long userId,String newEmail) {
        return Jwts.builder()
                .setSubject(newEmail)
                .setId(userId.toString())
                .signWith(secretKey)
                .compact();
    }

    public String getEmailFromToken(String token) throws Exception {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
}
