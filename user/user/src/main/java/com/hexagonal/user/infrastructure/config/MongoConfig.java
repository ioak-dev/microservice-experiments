package com.hexagonal.user.infrastructure.config;

import com.mongodb.client.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoConfig {

  private final MongoClient mongoClient;

  public MongoConfig(MongoClient mongoClient) {
    this.mongoClient = mongoClient;
  }

  @Bean
  public DynamicMongoFactory dynamicMongoFactory() {
    return new DynamicMongoFactory(mongoClient);
  }

}
