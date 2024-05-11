package com.example.cart.feign;

import com.example.cart.model.OrderProduct;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@FeignClient("order-service")
public interface OrderInterface {

  @PostMapping("/{userId}/{cartId}")
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<String> OrderProducts(@PathVariable(value = "cartId", required = false)
  String cartId, @RequestBody(required = false) OrderProduct orderProduct, @PathVariable("userId") String userId);
}
