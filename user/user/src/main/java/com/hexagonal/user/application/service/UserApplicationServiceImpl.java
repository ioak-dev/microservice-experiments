package com.hexagonal.user.application.service;

import com.hexagonal.user.application.port.UserRepository;
import com.hexagonal.user.domain.model.User;
import com.hexagonal.user.domain.service.UserService;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Slf4j
public class UserApplicationServiceImpl implements UserService{

  private final UserService userService;

  private final UserRepository userRepository;

  public UserApplicationServiceImpl(@Lazy UserService userService,@Lazy UserRepository userRepository){
    this.userRepository=userRepository;
    this.userService=userService;
  }
  @Override
  public User createUser(User user) {
    log.info("Creating user with details "+ ZonedDateTime.now());
    if(user.getId()==null){
      Random random = new Random();
      int id = 100000 + random.nextInt(900000);
      user.setId(String.valueOf(id));
    }
    return userRepository.save(user);
  }

  @Override
  public User getUserById(String id) {
    log.info("Fetching user details with id "+ id);
    return userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
  }

  @Override
  public List<User> getAllUsers() {
    log.info("Fetching all user details");
    return userRepository.findAll();
  }

}
