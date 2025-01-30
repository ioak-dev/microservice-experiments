package com.example.bff.service;

import com.example.bff.client.CartClient;
import com.example.bff.client.ProductClient;
import com.example.bff.client.UserClient;
import com.example.bff.dto.CartDTO;
import com.example.bff.dto.ProductDTO;
import com.example.bff.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CacheService {

  private final UserClient userClient;
  private final ProductClient productClient;
  private final CartClient cartClient;

  public CacheService(UserClient userClient, ProductClient productClient, CartClient cartClient) {
    this.userClient = userClient;
    this.productClient = productClient;
    this.cartClient = cartClient;
  }

  @Cacheable(value = "userCache", key = "#userId")
  public UserDTO getUser(Long userId) {
    log.info("CACHE MISS: Fetching user from API for userId={}", userId);
    return userClient.getUserById(userId);
  }

  @Cacheable(value = "productCache", key = "#productId")
  public ProductDTO getProduct(Long productId) {
    log.info("CACHE MISS: Fetching product from API for productId={}", productId);
    return productClient.getProductById(productId);
  }

  @Cacheable(value = "cartCache", key = "#orderId")
  public CartDTO getCart(Long orderId) {
    log.info("CACHE MISS: Fetching cart from API for orderId={}", orderId);
    return cartClient.getCartItemsById(orderId);
  }
}
