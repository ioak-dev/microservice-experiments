package com.example.user.service;

import com.example.user.model.User;
import com.example.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;

  public void saveUser(User user) {
    userRepository.save(user);
  }

  public ResponseEntity<User> getUserById(String id) {
    return new ResponseEntity<>(userRepository.findById(id).get(), HttpStatus.OK);
  }

  public void deleterById(String id) {
    userRepository.deleteById(id);
  }

  public User updateUser(String id, User user) {
    if (id != null) {
      User existingUser = userRepository.findById(id)
          .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
      existingUser.setName(user.getName());
      existingUser.setAddress(user.getAddress());
      existingUser.setPhoneNumber(user.getPhoneNumber());
      return userRepository.save(existingUser);
    }
    return null;
  }

}
