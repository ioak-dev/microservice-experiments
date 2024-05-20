package com.example.order.paymentConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class PaymentClientConfig {

  @Bean
  public WebClient paymentWebClient() {
    return WebClient.builder()
        .baseUrl("http://localhost:9090")
        .build();
  }

  @Bean
  public PaymentClient paymentClientClient() {
    HttpServiceProxyFactory httpServiceProxyFactory
        = HttpServiceProxyFactory.builderFor(WebClientAdapter.create(paymentWebClient()))
        .build();
    return httpServiceProxyFactory.createClient(PaymentClient.class);
  }

}
