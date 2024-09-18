package com.example.securitymicrosoftauth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Controller
@Slf4j
public class MicrosoftAuthController {

  @PostMapping("/userinfo")
  public ResponseEntity<?> getUserInfo(@RequestParam String accessToken) {
    log.info("Received access token: {}", accessToken);
    String userInfoUrl = "https://graph.microsoft.com/v1.0/me";

    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + accessToken);
    headers.set("Content-Type", "application/json");
    HttpEntity<String> entity = new HttpEntity<>(headers);
    RestTemplate restTemplate = new RestTemplate();

    try {
      ResponseEntity<UserInfo> response = restTemplate.exchange(userInfoUrl, HttpMethod.GET, entity, UserInfo.class);
      return ResponseEntity.ok(response.getBody());
    } catch (Exception e) {
      log.error("Error fetching user info: {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching user info");
    }

  }
}
