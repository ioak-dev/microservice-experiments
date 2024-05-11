package com.example.order.feign;


import com.example.order.model.OrderProduct;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("cart-service")
public interface CartInterface {

  @GetMapping("/{id}")
  public ResponseEntity<List<OrderProduct>> getProductsFromCart(@PathVariable("id") String id);
}
