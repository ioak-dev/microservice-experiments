package com.example.payment.controller;

import com.example.payment.service.PaymentService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
@RestController
public class PayPalController {


  private static final Logger LOG = LogManager.getLogger(PayPalController.class);

  @Autowired
  PaymentService service;

  @GetMapping("/")
  public String home() {
    return "index";
  }

  @PostMapping("/payment/create")
  public RedirectView createPayment(
      @RequestParam("method") String method,
      @RequestParam("amount") String amount,
      @RequestParam("currency") String currency,
      @RequestParam("description") String description
  ) {
    try {
      String cancelUrl = "http://localhost:8080/payment/cancel";
      String successUrl = "http://localhost:8080/payment/success";
      com.paypal.api.payments.Payment payment = service.createPayment(
          Double.valueOf(amount),
          currency,
          method,
          "sale",
          description,
          cancelUrl,
          successUrl
      );

      for (Links links: payment.getLinks()) {
        if (links.getRel().equals("approval_url")) {
          return new RedirectView(links.getHref());
        }
      }
    } catch (PayPalRESTException e) {
      LOG.error("Error occurred:: ", e);
    }
    return new RedirectView("/payment/error");
  }

  @GetMapping("/payment/success")
  public String paymentSuccess(
      @RequestParam("paymentId") String paymentId,
      @RequestParam("PayerID") String payerId
  ) {
    try {
      Payment payment = service.executePayment(paymentId, payerId);
      if (payment.getState().equals("approved")) {
        return "paymentSuccess";
      }
    } catch (PayPalRESTException e) {
      LOG.error("Error occurred:: ", e);
    }
    return "paymentSuccess";
  }

  @GetMapping("/payment/cancel")
  public String paymentCancel() {
    return "paymentCancel";
  }

  @GetMapping("/payment/error")
  public String paymentError() {
    return "paymentError";
  }


}
