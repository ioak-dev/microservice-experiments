package com.example.apigateway.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

  private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

  @Autowired
  private RouteValidator validator;

  @Value("${jwt.publicKeyPath}")
  private String publicKeyPath;

  public AuthenticationFilter() {
    super(Config.class);
  }

  @Override
  public GatewayFilter apply(Config config) {
    return (exchange, chain) -> {
      if (validator.isSecured.test(exchange.getRequest())) {
        if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
          logger.error("Missing authorization header");
          throw new RuntimeException("Missing authorization header");
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        try {
          String token = decodeToken(authHeader);
          logger.debug("Decoded token: {}", token);
          if (token != null) {
            PublicKey publicKey = getPublicKey(publicKeyPath);
            Claims claims = Jwts.parser()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
            String userId = claims.get("user_id", String.class);
            logger.debug("Parsed userId from token: {}", userId);
            if (userId != null) {
              UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                  userId, null, null);
              SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
          }
        } catch (ExpiredJwtException | MalformedJwtException | UnsupportedJwtException e) {
          logger.error("Invalid JWT token", e);
          throw new RuntimeException("Invalid JWT token", e);
        } catch (Exception e) {
          logger.error("Error parsing JWT token", e);
          throw new RuntimeException("Error parsing JWT token", e);
        }
      }
      return chain.filter(exchange);
    };
  }

  private PublicKey getPublicKey(String publicKeyPath) {
    try {
      Resource resource = new ClassPathResource(publicKeyPath);
      try (BufferedReader reader = new BufferedReader(new FileReader(resource.getFile()))) {
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
      logger.error("Failed to get public key", e);
      throw new RuntimeException("Failed to get public key", e);
    }
  }

  private String decodeToken(String authHeader) {
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      return authHeader.substring(7);
    }
    return null;
  }

  public static class Config {

  }
}
