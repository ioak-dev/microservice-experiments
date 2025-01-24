package com.example.bff.client;


import com.example.bff.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "https://fakestoreapi.com/")
public interface UserClient {

  @GetMapping("/users/{id}")
  UserDTO getUserById(@PathVariable Long id);

}
