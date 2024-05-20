package com.example.order.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

  private String id;
  private String name;
  private String description;
  private Float price;
  private Integer quantity;

  private String category;


}
