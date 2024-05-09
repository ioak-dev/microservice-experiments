package com.example.cart.feign;

import com.example.cart.model.OrderProduct;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("product-service")
public interface ProductInterface {

  @GetMapping("/{id}/{quantity}")
  public ResponseEntity<OrderProduct> getProductForCart(@PathVariable("id") String id,
      @PathVariable("quantity") Integer quantity);

  @PostMapping("/{id}/{quantity}")
  public ResponseEntity<OrderProduct> deleteProductFromCart(@PathVariable("id") String id,
      @PathVariable("quantity") Integer quantity);
}
