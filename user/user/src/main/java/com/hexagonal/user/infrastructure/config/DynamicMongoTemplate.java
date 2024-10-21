package com.hexagonal.user.infrastructure.config;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class DynamicMongoTemplate {

  private final DynamicMongoFactory dynamicMongoDbFactory;

  public DynamicMongoTemplate(@Lazy DynamicMongoFactory dynamicMongoDbFactory) {
    this.dynamicMongoDbFactory = dynamicMongoDbFactory;
  }

  public MongoTemplate getMongoTemplateForTenant(String tenantId) {
    MongoDatabaseFactory dbFactory = dynamicMongoDbFactory.getMongoDbFactoryForTenant(tenantId);
    return new MongoTemplate(dbFactory);
  }

}
