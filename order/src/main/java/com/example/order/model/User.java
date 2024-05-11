package com.example.order.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {


  @Id
  private String Id;

  private String name;

  private String address;

  private String phoneNumber;

}
