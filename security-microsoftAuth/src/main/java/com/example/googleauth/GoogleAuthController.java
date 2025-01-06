package com.example.googleauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GoogleAuthController {

  @Autowired
  private GoogleAuthService googleAuthService;

  @GetMapping("/api/auth")
  public ResponseEntity<?> authenticateUser(@RequestParam("code") String code) {
    try {
      if (code == null || code.isEmpty()) {
        throw new IllegalArgumentException("Invalid input: code is required");
      }

      var userDetails = googleAuthService.authenticateUser(code);
      return ResponseEntity.ok(userDetails);

    } catch (Exception e) {
      return ResponseEntity.status(500).body("Authentication failed: " + e.getMessage());
    }
  }
}
