package com.example.cart.service;

import ch.qos.logback.core.joran.conditional.IfAction;
import com.example.cart.feign.OrderInterface;
import com.example.cart.feign.ProductInterface;
import com.example.cart.model.Cart;
import com.example.cart.model.OrderProduct;
import com.example.cart.repository.CartRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CartService {

  @Autowired
  private CartRepository cartRepository;

  @Autowired
  private ProductInterface  productInterface;

  @Autowired
  private OrderInterface orderInterface;

  public ResponseEntity<String> addProductToCart(String id, String productId, Integer quantity) {
    Cart cart = cartRepository.findById(id).orElse(new Cart());
    OrderProduct orderProduct = productInterface.getProductForCart(productId, quantity).getBody();
    if (orderProduct != null) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body("Product out of Stock");
    }
    List<OrderProduct> orderProducts =cart.getProducts();
    orderProducts.add(orderProduct);
    cart.setProducts(orderProducts);
    cartRepository.save(cart);
    return ResponseEntity.status(HttpStatus.CREATED).body("Added product to Cart");
  }

  public ResponseEntity<String> removeProductFromCart(String id, String productId, Integer quantity) {
    Cart cart = cartRepository.findById(id).orElseThrow();
    OrderProduct orderProduct = productInterface.deleteProductFromCart(productId, quantity).getBody();
    List<OrderProduct> orderProducts =cart.getProducts();
    orderProducts.remove(orderProduct);
    cart.setProducts(orderProducts);
    cartRepository.save(cart);
    return ResponseEntity.status(HttpStatus.OK).body("Removed product from Cart");
  }

  public void orderFromCart(String id, String userId) {
    Cart cart = cartRepository.findById(id).orElseThrow();
    if (orderInterface.OrderProducts(id,null, userId).getStatusCode().is2xxSuccessful()){
      cartRepository.delete(cart);
    }
  }

  public ResponseEntity<List<OrderProduct>> getProductsFromCart(String id) {
    Cart cart = cartRepository.findById(id).orElseThrow();
    return ResponseEntity.status(HttpStatus.OK).body(cart.getProducts());
  }
}
