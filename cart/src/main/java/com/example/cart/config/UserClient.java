package com.example.cart.config;

import com.example.cart.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange
public interface UserClient {

  @GetExchange("/api/user/{id}")
  public ResponseEntity<User> getUser(@PathVariable String id);

}
