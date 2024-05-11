package com.example.order.service;

import com.example.order.feign.CartInterface;
import com.example.order.feign.ProductInterface;
import com.example.order.feign.UserInterface;
import com.example.order.model.Order;
import com.example.order.model.OrderProduct;
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
  private ProductInterface productInterface;

  @Autowired
  private UserInterface userInterface;

  @Autowired
  private CartInterface cartInterface;

  public ResponseEntity<String> orderProducts(String userId,
      String cartId, OrderProduct orderProduct) {
    Order order = new Order();
    if (cartId == null && orderProduct!= null) {
      List<OrderProduct> orderProducts = new ArrayList<>();
      productInterface.getProductForOrder(orderProduct);
      orderProducts.add(orderProduct);
      order.setProducts(orderProducts);
      order.setUser(userInterface.getUser(userId).getBody());
      //To do payment MS
      orderRepository.save(order);
      return ResponseEntity.status(HttpStatus.CREATED).body("Order placed successfully");
    }
    if (cartId != null) {
      order.setUser(userInterface.getUser(userId).getBody());
      List<OrderProduct> orderProducts = cartInterface.getProductsFromCart(cartId).getBody();
      order.setProducts(orderProducts);
      //To do payment MS
      orderRepository.save(order);
      return ResponseEntity.status(HttpStatus.CREATED).body("Order placed from cart successfully");
    }
    return ResponseEntity.status(HttpStatus.CONFLICT).body("Error occurred");
  }
}
