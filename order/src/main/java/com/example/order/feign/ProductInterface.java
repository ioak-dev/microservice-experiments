package com.example.order.feign;


import com.example.order.model.OrderProduct;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("product-service")
public interface ProductInterface {

  @GetMapping
  public ResponseEntity<OrderProduct> getProductForOrder(@RequestBody OrderProduct product);
}
