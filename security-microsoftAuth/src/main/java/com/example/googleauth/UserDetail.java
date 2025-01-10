package com.example.googleauth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetail {
  private String sub;
  private String name;
  private String givenName;
  private String familyName;
  private String picture;
  private String email;
  private boolean emailVerified;
}
