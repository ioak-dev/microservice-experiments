package com.hexagonal.user.infrastructure.persistence.sql;

import com.hexagonal.user.domain.model.User;
import com.hexagonal.user.infrastructure.config.JpaUserRepository;
import java.util.Optional;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile("sql")
public class SqlUserRepository {

/*  private final JpaUserRepository jpaRepository;

  public SqlUserRepository(JpaUserRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }
  public Optional<User> findById(String id) {
    return jpaRepository.findById(id);
  }

  public User save(User user) {
    return jpaRepository.save(user);
  }*/
}
