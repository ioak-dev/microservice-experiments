package com.hexagonal.user.domain.service;

import com.hexagonal.user.domain.model.User;
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;

public interface UserService {

  User createUser(User user, String tenantId);
  User getUserById(String id, String tenantId);
  List<User> getAllUsers( String tenantId);

}
