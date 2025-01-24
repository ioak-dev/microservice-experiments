package com.example.bff.client;


import com.example.bff.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "product", url = "https://fakestoreapi.com/")
public interface ProductClient {

  @GetMapping("/products/{id}")
  ProductDTO getProductById(@PathVariable Long id);
}
