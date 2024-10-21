package com.hexagonal.user.infrastructure.config;

import com.mongodb.client.MongoClient;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

public class DynamicMongoFactory {

  private final MongoClient mongoClient;
  private static final String DEFAULT_DB_NAME = "defaultDb";

  public DynamicMongoFactory(MongoClient mongoClient) {
    this.mongoClient = mongoClient;
  }

  public MongoDatabaseFactory getMongoDbFactoryForTenant(String tenantId) {
    String databaseName = tenantId != null && !tenantId.isEmpty() ? "tenant_" + tenantId : DEFAULT_DB_NAME;
    return new SimpleMongoClientDatabaseFactory(mongoClient, databaseName);
  }

}
