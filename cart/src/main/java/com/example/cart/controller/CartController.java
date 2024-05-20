package com.example.cart.controller;

import com.example.cart.model.Cart;
import com.example.cart.model.OrderRequest;
import com.example.cart.model.Product;
import com.example.cart.service.CartService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
public class CartController {

  @Autowired
  private CartService cartService;

  @PostMapping("/{id}/add/{productId}/{quantity}")
  public ResponseEntity<Cart> addProductToCart(@PathVariable("id") String id,
      @PathVariable("productId") String productId, @PathVariable("quantity") Integer quantity) {
    return cartService.addProductToCart(id,productId, quantity);
  }

  @PostMapping("/{id}/remove/{productId}")
  public ResponseEntity<Cart> removeProductFromCart(@PathVariable("id") String id,
      @PathVariable("productId") String productId) {
    return cartService.removeProductFromCart(id,productId);
  }

  @PostMapping("/{id}/order/{userId}/{prodId}")
  public void orderFromCart(@PathVariable("id") String id, @PathVariable("userId") String userId
  ,@PathVariable("prodId") String prodId,@RequestBody OrderRequest orderRequest) {
    cartService.orderFromCart(id, userId,prodId, orderRequest);
  }

  @GetMapping("/{id}")
  public ResponseEntity<List<Product>> getProductsFromCart(@PathVariable("id") String id) {
    return cartService.getProductsFromCart(id);
  }

  @PostMapping("/{userId}")
  public void createCartForUserId(@PathVariable String userId){
    cartService.createCartForUser(userId);
  }
}
