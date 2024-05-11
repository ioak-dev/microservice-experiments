package com.example.order.feign;

import com.example.order.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("user-service")
public interface UserInterface {

  /*@GetMapping("/{id}")
  public ResponseEntity<User> getUser(@PathVariable String id);*/
}
