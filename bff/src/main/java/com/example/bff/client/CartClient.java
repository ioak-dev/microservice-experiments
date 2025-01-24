package com.example.bff.client;


import com.example.bff.dto.CartDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "cart",url = "https://fakestoreapi.com/carts")
public interface CartClient {

  @GetMapping("/{id}")
  CartDTO getCartItemsById(@PathVariable Long id);

}
