package com.hexagonal.user.application.port;

import com.hexagonal.user.domain.model.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
  Optional<User> findById(String id, String tenantId);
  User save(User user, String tenantId);

  List<User> findAll(String tenantId);

}
