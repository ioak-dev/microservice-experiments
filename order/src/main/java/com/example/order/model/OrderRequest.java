package com.example.order.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {

  private PaymentType paymentType;

  private Integer quantity;

}
