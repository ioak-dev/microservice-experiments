package com.example.user.service;

import com.example.user.model.User;
import com.example.user.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {
  private static final Logger LOG = LogManager.getLogger(UserService.class);

  @Autowired
  private UserRepository userRepository;


  public User saveUser(User user) {
    LOG.info("Saving the user details to db");
    return userRepository.save(user);
  }

  public ResponseEntity<User> getUserById(String id) {
    User user=userRepository.findById(id).orElseThrow(()->
        new ResponseStatusException(HttpStatus.NOT_FOUND));
    return new ResponseEntity<>(user, HttpStatus.OK);
  }

  public void deleterById(String id) {
    userRepository.deleteById(id);
  }

  public User updateUser(String id, User user) {
    LOG.info("Updating the user details");
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
