package com.example.order.model;

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
@Document(collection = "order")
@Builder
public class Order {

  @Id
  private String id;
  private User user;
  private List<Product> products;

  private OrderStatus orderStatus;

  private PaymentType paymentType;

  private Integer quantity;

  private Integer totalPrice;

  private Payment payment;
}
