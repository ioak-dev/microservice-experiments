package com.example.bff.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebAppDTO {
  private UserDTO user;
  private ProductDTO product;
  private CartDTO cart;
}
