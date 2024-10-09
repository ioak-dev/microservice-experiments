package com.hexagonal.user.domain.service;

import com.hexagonal.user.domain.model.User;
import java.util.List;

public interface UserService {

  User createUser(User user);
  User getUserById(String id);
  List<User> getAllUsers();

}
