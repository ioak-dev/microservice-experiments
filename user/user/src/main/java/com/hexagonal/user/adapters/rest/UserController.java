package com.hexagonal.user.adapters.rest;

import com.hexagonal.user.domain.model.User;
import com.hexagonal.user.domain.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/{tenantId}/users")
public class UserController {


  private final UserService userApplicationService;

  public UserController(@Lazy UserService userApplicationService) {
    this.userApplicationService = userApplicationService;
  }

  @PostMapping
  public ResponseEntity<User> createUser(@RequestBody User request, @PathVariable String tenantId) {
    User createdUser = userApplicationService.createUser(request,tenantId);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
  }

  @GetMapping("/{id}")
  public ResponseEntity<User> getUser(@PathVariable String id,@PathVariable String tenantId) {
    return ResponseEntity.ok(userApplicationService.getUserById(id,tenantId));
  }

  @GetMapping("/all")
  public ResponseEntity<?> getAllUser(@PathVariable String tenantId) {
    return ResponseEntity.ok(userApplicationService.getAllUsers(tenantId));
  }

}
