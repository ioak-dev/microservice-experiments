package com.example.order.paymentConfig;

import com.example.order.model.Payment;
import com.example.order.model.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange
public interface PaymentClient {

  @PostExchange("/api/payment/pay/{orderId}")
  Payment doPaymentForOrder(@PathVariable("orderId") String orderId);

}
