package com.example.payment.service;

import com.example.payment.repository.PaymentRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PaymentService {

  private static final Logger LOG = LogManager.getLogger(PaymentService.class);

  @Autowired
  private APIContext apiContext;

  @Autowired
  PaymentRepository paymentRepository;


  public Payment createPayment(
      Double total,
      String currency,
      String method,
      String intent,
      String description,
      String cancelUrl,
      String successUrl) throws PayPalRESTException {

    LOG.info("Creating payment object");
    Amount amount = new Amount();
    amount.setCurrency(currency);
    amount.setTotal(String.format(Locale.forLanguageTag(currency), "%.2f", total));
    Transaction transaction = new Transaction();
    transaction.setDescription(description);
    transaction.setAmount(amount);

    List<Transaction> transactions = new ArrayList<>();
    transactions.add(transaction);

    Payer payer = new Payer();
    payer.setPaymentMethod(method.toString());

    Payment payment = new Payment();
    payment.setIntent(intent.toString());
    payment.setPayer(payer);
    payment.setTransactions(transactions);
    RedirectUrls redirectUrls = new RedirectUrls();
    redirectUrls.setCancelUrl(cancelUrl);
    redirectUrls.setReturnUrl(successUrl);
    payment.setRedirectUrls(redirectUrls);

    return payment.create(apiContext);
  }

  public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
    Payment payment = new Payment();
    payment.setId(paymentId);
    PaymentExecution paymentExecute = new PaymentExecution();
    paymentExecute.setPayerId(payerId);
    return payment.execute(apiContext, paymentExecute);
  }


  public com.example.payment.model.Payment doPaymentForOrder(String orderId) {
    try {
      LOG.info("Execute payment for the order : {}" ,orderId);
      Resource resource = new ClassPathResource("classpath:/PayPalResponse.json");
      String content = resource.getContentAsString(StandardCharsets.UTF_8);
      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode jsonNode = objectMapper.readTree(content);
      JsonNode transactionsNode = jsonNode.path("transactions").get(0);
      JsonNode amountNode = transactionsNode.path("amount");
      double price = amountNode.path("total").asDouble();
      String currency = amountNode.path("currency").asText();
      String method = jsonNode.path("payer").path("payment_method").asText();
      String description = transactionsNode.path("description").asText();
      com.example.payment.model.Payment payment = com.example.payment.model.Payment.builder()
          .orderId(orderId)
          .price(price)
          .paymentReferenceNumber(String.valueOf(UUID.randomUUID()))
          .currency(currency)
          .method(method)
          .description(description)
          .build();
      return paymentRepository.save(payment);
    } catch (Exception e) {
      LOG.error(e.getMessage());
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
