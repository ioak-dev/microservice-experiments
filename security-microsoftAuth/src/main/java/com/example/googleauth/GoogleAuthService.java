package com.example.googleauth;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class GoogleAuthService {

  @Autowired
  private GoogleAuthUtil googleAuthUtil;

  public UserDetails authenticateUser(String code) throws Exception {
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
    var response = restTemplate.exchange(userInfoUrl, org.springframework.http.HttpMethod.GET, request, UserDetail.class);

    if (response.getStatusCode().is2xxSuccessful()) {
      UserDetail userDetail = response.getBody();
      if (userDetail != null) {
        UserDetails userDetails = new UserDetails();
        userDetails.setMessage("Authentication successful");
        userDetails.setUserDetails(userDetail);
        return userDetails;
      }
      else {
        throw new RuntimeException("User details not found");
      }
    } else {
      throw new RuntimeException("Failed to fetch user details from Google");
    }
  }
}