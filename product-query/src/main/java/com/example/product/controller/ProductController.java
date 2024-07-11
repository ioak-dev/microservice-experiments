package com.example.product.controller;


import com.example.product.model.Product;
import com.example.product.service.ProductService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/product/query")
public class ProductController {

  @Autowired
  private ProductService productservice;

  @GetMapping("/all")
  @ResponseStatus(HttpStatus.OK)
  public List<Product> getAllProducts() {
    return productservice.getAllProducts();
  }

  @GetMapping("/{id}/{quantity}")
  public ResponseEntity<Product> getProductForCart(@PathVariable("id") String id,
      @PathVariable("quantity") String quantity) {
    Product product = productservice.getProductForCart(id, Integer.parseInt(quantity));
    return new ResponseEntity<>(product, HttpStatus.FOUND);
  }

  @GetMapping("/{productId}")
  public ResponseEntity<Product> getProductForOrder(@PathVariable String productId) {
    return productservice.getProductForOrder(productId);
  }

}
