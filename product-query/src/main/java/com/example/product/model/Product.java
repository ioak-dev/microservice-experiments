package com.example.product.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "product.query")
@Builder
public class Product {

  @Id
  private String id;
  private String name;
  private String description;
  private Float price;
  private Integer quantity;

  private String category;

}
