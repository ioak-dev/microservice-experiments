package com.example.bff.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
  private Long id;
  private String title;
  private Double price;

  private String description;

  private String category;

}
