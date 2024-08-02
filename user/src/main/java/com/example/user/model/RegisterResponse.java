package com.example.user.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterResponse {

  private String userName;
  private String email;

}
