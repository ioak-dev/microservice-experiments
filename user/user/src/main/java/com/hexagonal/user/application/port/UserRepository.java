package com.hexagonal.user.application.port;

import com.hexagonal.user.domain.model.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
  Optional<User> findById(String id);
  User save(User user);

  List<User> findAll();

}
