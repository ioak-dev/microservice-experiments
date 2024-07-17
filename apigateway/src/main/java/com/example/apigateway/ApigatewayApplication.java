package com.example.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@SpringBootApplication
@EnableDiscoveryClient
@PropertySources(@PropertySource("classpath:application.properties"))
public class ApigatewayApplication {
  //TODO : Implement security service for gateway
	public static void main(String[] args) {
		SpringApplication.run(ApigatewayApplication.class, args);
	}

}
