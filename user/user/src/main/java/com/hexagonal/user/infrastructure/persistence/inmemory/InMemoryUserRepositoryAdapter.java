package com.hexagonal.user.infrastructure.persistence.inmemory;


import com.hexagonal.user.application.port.UserRepository;
import com.hexagonal.user.domain.model.User;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile("memory")
@Slf4j
public class InMemoryUserRepositoryAdapter implements UserRepository {

  private final Map<String, User> inMemoryDatabase = new HashMap<>();
  @Override
  public Optional<User> findById(String id,String tenantId) {
    log.info("Fetching user details with id from in memory "+ id);
    return Optional.ofNullable(inMemoryDatabase.get(id));
  }

  @Override
  public User save(User user,String tenantId) {
    log.info("Saving user details in memory db");
    inMemoryDatabase.put(user.getId(), user);
    return user;
  }

  @Override
  public List<User> findAll(String tenantId) {
    log.info("Fetching user details from in memory");
    List<User> users=new ArrayList<>();
    for(Map.Entry<String,User> entity:inMemoryDatabase.entrySet()){
       users.add(entity.getValue());
    }
   return users;
  }
}
