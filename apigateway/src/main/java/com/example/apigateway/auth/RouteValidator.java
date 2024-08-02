package com.example.apigateway.auth;

import java.util.List;
import java.util.function.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity.CsrfSpec;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.stereotype.Component;

@Configuration
@EnableWebFluxSecurity
public class RouteValidator {

  @Bean
  public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
    http.csrf(CsrfSpec::disable)
        .authorizeExchange(exchanges -> exchanges
            .pathMatchers("/**").permitAll()
            .anyExchange().authenticated());
    return http.build();
  }
}
