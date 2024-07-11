package com.example.cart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class WebclientConfig {

  @Bean
  public WebClient userWebClient() {
    return WebClient.builder()
        .baseUrl("http://user-service:8084")
        .build();
  }

  @Bean
  public UserClient userClient() {
    HttpServiceProxyFactory httpServiceProxyFactory
        = HttpServiceProxyFactory.builderFor(WebClientAdapter.create(userWebClient()))
        .build();
    return httpServiceProxyFactory.createClient(UserClient.class);
  }

}
