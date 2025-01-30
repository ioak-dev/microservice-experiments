package com.example.bff.service;

import com.example.bff.dto.CartDTO;
import com.example.bff.dto.ProductDTO;
import com.example.bff.dto.UserDTO;
import com.example.bff.dto.WebAppDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WebAppService {

  private final CacheService cacheService;

  public WebAppService(CacheService cacheService) {
    this.cacheService = cacheService;
  }

  public WebAppDTO getAggregatedData(Long userId, Long productId, Long orderId) {
    log.info("Fetching aggregated data for userId={}, productId={}, orderId={}", userId, productId, orderId);
    UserDTO user = cacheService.getUser(userId);
    ProductDTO product = cacheService.getProduct(productId);
    CartDTO cart = cacheService.getCart(orderId);
    return new WebAppDTO(user, product, cart);
  }
}
