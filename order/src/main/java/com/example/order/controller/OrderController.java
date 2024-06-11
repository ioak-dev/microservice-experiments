package com.example.order.controller;

import com.example.order.model.Order;
import com.example.order.model.OrderRequest;
import com.example.order.service.OrderService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

  private static final Logger LOG = LogManager.getLogger(OrderController.class);


  @PostMapping("/{userId}/{prodId}")
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<Order> orderProducts(@PathVariable("prodId") String prodId,
      @PathVariable("userId") String userId,
      @RequestBody OrderRequest request) {
    LOG.info("Order product : {}, for the user : {}", prodId, userId);
    return orderService.orderProducts(userId, prodId, request);
  }
}
