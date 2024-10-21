package com.hexagonal.user.infrastructure.persistence.mongo;

import com.hexagonal.user.application.port.UserRepository;
import com.hexagonal.user.domain.model.User;
import com.hexagonal.user.infrastructure.config.DynamicMongoTemplate;
import java.util.List;
import java.util.Optional;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Profile(("mongo"))
public class MongoUserRepository implements UserRepository {

  private final DynamicMongoTemplate dynamicMongoTemplate;

  public MongoUserRepository(DynamicMongoTemplate dynamicMongoTemplate) {
    this.dynamicMongoTemplate = dynamicMongoTemplate;
  }

  private MongoTemplate getTenantMongoTemplate(String tenantId) {
    return dynamicMongoTemplate.getMongoTemplateForTenant(tenantId);
  }

  @Override
  public Optional<User> findById(String id, String tenantId) {
    return Optional.ofNullable(getTenantMongoTemplate(tenantId).findById(id, User.class));
  }

  @Override
  public List<User> findAll(String tenantId) {
    return getTenantMongoTemplate(tenantId).findAll(User.class);
  }

  @Override
  public User save(User user, String tenantId) {
    return getTenantMongoTemplate(tenantId).save(user);
  }

}
