package com.example.security.controller;

import com.example.security.UserClient.UserClient;
import com.example.security.dto.AuthRequest;
import com.example.security.dto.User;
import com.example.security.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class Controller {

  @Autowired
  private AuthService service;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private UserClient userClient;

  @PostMapping("/register")
  public User addNewUser(@RequestBody User user) {
    return userClient.saveUser(user);
  }

  @PostMapping("/token")
  public String getToken(@RequestBody AuthRequest authRequest) {
    Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
    if (authenticate.isAuthenticated()) {
      return service.generateToken(authRequest.getUsername());
    } else {
      throw new RuntimeException("invalid access");
    }
  }

  @GetMapping("/validate")
  public String validateToken(@RequestParam("token") String token) {
    service.validateToken(token);
    return "Token is valid";
  }
}