package com.example.cart.orderconfig;

import com.example.cart.productconfig.ProductClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class OrderClientConfig {

  @Bean
  public WebClient orderWebClient() {
    return WebClient.builder()
        .baseUrl("http://localhost:8085")
        .build();
  }

  @Bean
  public OrderClient orderClient() {
    HttpServiceProxyFactory httpServiceProxyFactory
        = HttpServiceProxyFactory.builderFor(WebClientAdapter.create(orderWebClient()))
        .build();
    return httpServiceProxyFactory.createClient(OrderClient.class);
  }


}
