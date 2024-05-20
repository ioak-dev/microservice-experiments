package com.example.cart.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "cart")
@Builder
public class Cart {

  @Id
  private String id;
  private String userCartId;
  private String userId;
  private List<Product> products;

  private Integer quantity;


}
