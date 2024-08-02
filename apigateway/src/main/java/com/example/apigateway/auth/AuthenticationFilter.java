package com.example.apigateway.auth;

import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

  private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

  @Autowired
  private JwtUtil jwtUtil;

  public AuthenticationFilter() {
    super(Config.class);
  }

  @Override
  public GatewayFilter apply(Config config) {
    return (exchange, chain) -> {
      ServerHttpRequest request = exchange.getRequest();
      if (!request.getHeaders().containsKey("Authorization")) {
        logger.error("Missing authorization header");
        return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Missing authorization header"));
      }

      String authHeader = request.getHeaders().getFirst("Authorization");
      if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        logger.error("Invalid authorization header");
        return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid authorization header"));
      }

      try {
        String token = authHeader.substring(7);
        Claims claims = jwtUtil.extractClaims(token);
        String userId = claims.get("user_id", String.class);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
            userId, null, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return chain.filter(exchange);
      } catch (Exception e) {
        logger.error("Invalid JWT token", e);
        return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid JWT token", e));
      }
    };
  }

  public static class Config {

  }
}
