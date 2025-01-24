package com.example.bff.service;

import com.example.bff.client.CartClient;
import com.example.bff.client.ProductClient;
import com.example.bff.client.UserClient;
import com.example.bff.dto.CartDTO;
import com.example.bff.dto.ProductDTO;
import com.example.bff.dto.UserDTO;
import com.example.bff.dto.WebAppDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WebAppService {

  private final UserClient userClient;
  private final ProductClient productClient;
  private final CartClient cartClient;

  public WebAppService(UserClient userClient, ProductClient productClient, CartClient cartClient) {
    this.userClient = userClient;
    this.productClient = productClient;
    this.cartClient = cartClient;
  }

  public WebAppDTO getAggregatedData(Long userId, Long productId, Long orderId) {
    log.debug("Get data for Web app");
    UserDTO user = userClient.getUserById(userId);
    ProductDTO product = productClient.getProductById(productId);
    CartDTO cart = cartClient.getCartItemsById(orderId);
    return new WebAppDTO(user, product, cart);
  }

}
