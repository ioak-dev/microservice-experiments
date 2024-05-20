package com.example.order.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

  private String paymentId;


  private String orderId;

  private String paymentReferenceNumber;

  private double price;
  private String currency;
  private String method;
  private String intent;
  private String description;

}
