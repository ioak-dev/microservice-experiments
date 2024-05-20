package com.example.payment.controller;

import com.example.payment.model.Payment;
import com.example.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {


  @Autowired
  PaymentService paymentService;


  @PostMapping("/pay/{orderId}")
  public Payment doPaymentForOrder(@PathVariable("orderId") String orderId){
   return paymentService.doPaymentForOrder(orderId);
  }

}
