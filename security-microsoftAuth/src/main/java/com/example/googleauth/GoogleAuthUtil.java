package com.example.googleauth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public class GoogleAuthUtil {

  @Value("${google.client.id}")
  private String clientId;

  @Value("${google.client.secret}")
  private String clientSecret;

  @Value("${google.redirect.uri}")
  private String redirectUri;

  private static final String TOKEN_SERVER_URL = "https://oauth2.googleapis.com/token";

  public Map<String, String> exchangeCodeForToken(String code) throws Exception {
    GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(
        new NetHttpTransport(),
        JacksonFactory.getDefaultInstance(),
        TOKEN_SERVER_URL,
        clientId,
        clientSecret,
        code,
        redirectUri
    ).execute();

    Map<String, String> tokens = new HashMap<>();
    tokens.put("accessToken", tokenResponse.getAccessToken());
    tokens.put("idToken", tokenResponse.getIdToken());
    return tokens;
  }

  public GoogleIdToken.Payload verifyIdToken(String idToken) throws Exception {
    GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
        new NetHttpTransport(),
        JacksonFactory.getDefaultInstance()
    )
        .setAudience(Collections.singletonList(clientId))
        .build();

    GoogleIdToken googleIdToken = verifier.verify(idToken);
    if (googleIdToken != null) {
      return googleIdToken.getPayload();
    } else {
      throw new RuntimeException("Invalid ID Token");
    }
  }
}