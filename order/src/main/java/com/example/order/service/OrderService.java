package com.example.order.service;

import com.example.order.config.UserClient;
import com.example.order.model.Order;
import com.example.order.model.OrderRequest;
import com.example.order.model.OrderStatus;
import com.example.order.model.Payment;
import com.example.order.model.PaymentType;
import com.example.order.model.Product;
import com.example.order.paymentConfig.PaymentClient;
import com.example.order.productConfig.ProductClient;
import com.example.order.repository.OrderRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderService {

  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private ProductClient productInterface;

  @Autowired
  private UserClient userClient;


  @Autowired
  private PaymentClient paymentClient;

  public ResponseEntity<Order> orderProducts(String userId, String prodId, OrderRequest request) {
    Order order = new Order();
    if (prodId != null) {
      List<Product> orderProducts = new ArrayList<>();
      Product product = productInterface.getProductForOrder(prodId).getBody();
      orderProducts.add(product);
      order.setProducts(orderProducts);
      order.setUser(userClient.getUser(userId).getBody());
      order.setPaymentType(request.getPaymentType());
      order.setOrderStatus(OrderStatus.ORDER_INITIATED);
      order.setQuantity(request.getQuantity());
      if (request.getPaymentType().equals(PaymentType.COD)) {
        order.setOrderStatus(OrderStatus.ORDERED_PAYMENT_PENDING);
      }
      order.setTotalPrice((int) (request.getQuantity() * product.getPrice()));
     Order responseOrder= orderRepository.save(order);
      if(request.getPaymentType().equals(PaymentType.UPI)) {
        Payment payment = paymentClient.doPaymentForOrder(responseOrder.getId());
        if (payment != null && payment.getPaymentReferenceNumber() != null) {
          responseOrder.setOrderStatus(OrderStatus.ORDERED);
          responseOrder.setPayment(payment);
          orderRepository.save(responseOrder);
        }
      }
      return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }
    return null;
  }
}
