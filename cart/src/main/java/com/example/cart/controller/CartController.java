package com.example.cart.controller;

import com.example.cart.model.Cart;
import com.example.cart.model.OrderProduct;
import com.example.cart.service.CartService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
public class CartController {

  @Autowired
  private CartService cartService;

  @PostMapping("/{id}/add/{productId}/{quantity}")
  public ResponseEntity<String> addProductToCart(@PathVariable("id") String id,
      @PathVariable("productId") String productId, @PathVariable("quantity") Integer quantity) {
    return cartService.addProductToCart(id,productId, quantity);
  }

  @PostMapping("/{id}/remove/{productId}/{quantity}")
  public ResponseEntity<String> removeProductFromCart(@PathVariable("id") String id,
      @PathVariable("productId") String productId, @PathVariable("quantity") Integer quantity) {
    return cartService.removeProductFromCart(id,productId, quantity);
  }

  @PostMapping("/{id}/order/{userId}")
  public void orderFromCart(@PathVariable("id") String id, @PathVariable("userId") String userId) {
    cartService.orderFromCart(id, userId);
  }

  @GetMapping("/{id}")
  public ResponseEntity<List<OrderProduct>> getProductsFromCart(@PathVariable("id") String id) {
    return cartService.getProductsFromCart(id);
  }
}
