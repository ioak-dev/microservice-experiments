package com.example.apigateway.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
public class JwtUtil {

  private PublicKey publicKey;

  @PostConstruct
  public void init() {
    this.publicKey = getPublicKey();
  }

  public Claims extractClaims(String token) {
    return Jwts.parser()
        .setSigningKey(publicKey)
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  private PublicKey getPublicKey() {
    String publicKey = "public.pem";
    try {
      Resource resource = new ClassPathResource(publicKey);
      try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
        StringBuilder pemContent = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
          if (line.contains("PUBLIC KEY")) {
            continue;
          }
          pemContent.append(line);
        }
        byte[] decodedKey = Base64.getDecoder().decode(pemContent.toString());
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
      }
    } catch (Exception e) {
      throw new RuntimeException("Failed to get public key", e);
    }
  }
}
