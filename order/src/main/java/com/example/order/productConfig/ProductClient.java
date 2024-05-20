package com.example.order.productConfig;

import com.example.order.model.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange
public interface ProductClient {

  @GetExchange("/api/product/{productId}")
  public ResponseEntity<Product> getProductForOrder(@PathVariable("productId") String productId);

  @PostMapping("/api/product/{id}/{quantity}")
  public ResponseEntity<Product> deleteProductFromCart(@PathVariable("id") String id,
      @PathVariable("quantity") Integer quantity);

}
