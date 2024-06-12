package com.example.product.controller;


import com.example.product.dto.ProductEvent;
import com.example.product.model.Product;
import com.example.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/product")
public class ProductController {

  @Autowired
  private ProductService productservice;

  @PostMapping("/{id}/{quantity}")
  public ResponseEntity<Product> deleteProductFromCart(@PathVariable("id") String id,
      @PathVariable("quantity") Integer quantity) {
    Product product = productservice.deleteProductFromCart(id, quantity);
    return new ResponseEntity<>(product, HttpStatus.OK);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Product createProduct(@RequestBody Product productRequest) {
   return productservice.createProduct(productRequest);
  }

  @PostMapping("/{id}")
  @ResponseStatus(HttpStatus.CREATED)
  public void addProductToInventory(@PathVariable("id") String id,
      @RequestBody Integer quantity) {
    productservice.addProductToInventory(id, quantity);
  }

  @PutMapping("/{id}")
  public Product updateProduct(@PathVariable("id") String id, @RequestBody Product productRequest) {
    return productservice.updateProduct(id, productRequest);
  }

  @DeleteMapping("/{id}")
  public void deleteProduct(@PathVariable("id") String id) {
    productservice.deleteProduct(id);
  }

}
