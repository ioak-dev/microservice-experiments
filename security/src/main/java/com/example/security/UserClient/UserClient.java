package com.example.security.UserClient;


import com.example.security.dto.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange
public interface UserClient {

  @GetExchange("/api/user/{id}")
  public ResponseEntity<User> getUser(@PathVariable String id);

  @PostExchange("/api/user")
  public User saveUser(@RequestBody User user);
}
