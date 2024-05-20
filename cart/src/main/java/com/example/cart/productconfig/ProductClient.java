package com.example.cart.productconfig;

import com.example.cart.model.Product;
import java.util.List;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange
public interface ProductClient {

  @GetExchange("/api/product/all")
  public List<Product> getAllProducts();

}
