package com.example.securitymicrosoftauth;

import java.util.List;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserInfo {

  private String userPrincipalName;
  private String id;
  private String displayName;
  private String surname;
  private String givenName;
  private String preferredLanguage;
  private String mail;
  private String mobilePhone;
  private String jobTitle;
  private String officeLocation;
  private List<String> businessPhones;
}
