package com.example.user.controller;

import com.example.user.model.User;
import com.example.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {


  @Autowired
  private UserService userService;

  @PostMapping
  public User saveUser(@RequestBody User user){
    return userService.saveUser(user);
  }

  @GetMapping("/{id}")
  public ResponseEntity<User> getUser(@PathVariable String id){
   return userService.getUserById(id);
  }

  @DeleteMapping("/{id}")
  public void deleteUser(@PathVariable String id){
    userService.deleterById(id);
  }

  @PutMapping("/{id}")
  public User updateUser(@PathVariable String id,@RequestBody User user){
   return userService.updateUser(id,user);
  }

}
