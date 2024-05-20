package com.example.cart.orderconfig;

import com.example.cart.model.OrderRequest;
import com.example.cart.model.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange
public interface OrderClient {
  @PostExchange("/api/order/{userId}/{prodId}")
  public ResponseEntity<String> OrderProducts(@PathVariable(value = "prodId")
  String prodId, @RequestBody(required = false) OrderRequest orderProduct, @PathVariable("userId") String userId);


}
