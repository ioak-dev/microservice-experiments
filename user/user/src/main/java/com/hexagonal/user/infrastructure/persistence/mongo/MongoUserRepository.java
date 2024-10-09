package com.hexagonal.user.infrastructure.persistence.mongo;

import com.hexagonal.user.application.port.UserRepository;
import com.hexagonal.user.domain.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Profile(("mongo"))
public class MongoUserRepository implements UserRepository {

  private final MongoTemplate mongoTemplate;

  public MongoUserRepository(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  @Override
  public Optional<User> findById(String id) {
    return Optional.ofNullable(mongoTemplate.findById(id, User.class));
  }

  @Override
  public User save(User user) {
    return mongoTemplate.save(user);
  }

  @Override
  public List<User> findAll() {
    return mongoTemplate.findAll(User.class);
  }

}
