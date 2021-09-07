package com.example.firstSpringProgect.providers;

import com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithm;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class JwtConfirmProvider {
    private String secretKeyString = "sdfgh";
    private SecretKey secretKey;

    @PostConstruct
    private void initialize() {
        try {
            secretKey = new SecretKeySpec(
                    MessageDigest.getInstance("SHA-256")
                            .digest(secretKeyString.getBytes()),
                    "HmacSHA256"
            );
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public String generateToken(Long userId,String newEmail){
        return Jwts.builder()
                .setSubject(newEmail)
                .setId(userId.toString())
                .signWith(secretKey)
                .compact();
    }

    public String getEmailFromToken(String token){
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
