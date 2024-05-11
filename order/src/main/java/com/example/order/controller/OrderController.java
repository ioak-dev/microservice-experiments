package com.example.order.controller;

import com.example.order.model.OrderProduct;
import com.example.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
public class OrderController {

  @Autowired
  private OrderService orderService;

  @PostMapping("/{userId}/{cartId}")
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<String> OrderProducts(@PathVariable
  String cartId, @RequestBody(required = false) OrderProduct orderProduct, @PathVariable String userId) {
    return orderService.orderProducts(userId, cartId, orderProduct);
  }
}
