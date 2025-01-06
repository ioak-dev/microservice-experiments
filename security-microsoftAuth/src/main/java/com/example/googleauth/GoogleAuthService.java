package com.example.googleauth;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class GoogleAuthService {

  @Autowired
  private GoogleAuthUtil googleAuthUtil;

  public Map<String, Object> authenticateUser(String code) throws Exception {
    Map<String, String> tokens = googleAuthUtil.exchangeCodeForToken(code);
    String idToken = tokens.get("idToken");
    String accessToken = tokens.get("accessToken");

    var payload = googleAuthUtil.verifyIdToken(idToken);

    // Fetch user details
    RestTemplate restTemplate = new RestTemplate();
    String userInfoUrl = "https://www.googleapis.com/oauth2/v3/userinfo";

    var headers = new org.springframework.http.HttpHeaders();
    headers.setBearerAuth(accessToken);

    var request = new org.springframework.http.HttpEntity<>(headers);
    var response = restTemplate.exchange(userInfoUrl, org.springframework.http.HttpMethod.GET, request, Map.class);

    if (response.getStatusCode().is2xxSuccessful()) {
      return response.getBody();
    } else {
      throw new RuntimeException("Failed to fetch user details from Google");
    }
  }
}