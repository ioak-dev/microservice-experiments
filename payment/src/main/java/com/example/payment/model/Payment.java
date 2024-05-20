package com.example.payment.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "payment")
public class Payment {

  @Id
  private String id;

  private String orderId;

  private String paymentReferenceNumber;

  private double price;
  private String currency;
  private String method;
  private String intent;
  private String description;

}
