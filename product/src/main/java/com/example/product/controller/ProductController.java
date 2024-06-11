package com.example.product.controller;

import com.example.product.model.Product;
import com.example.product.service.ProductService;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/product")
public class ProductController {


  private static final Logger LOG = LogManager.getLogger(ProductController.class);
  @Autowired
  private ProductService productservice;

  @GetMapping("/all")
  @ResponseStatus(HttpStatus.OK)
  public List<Product> getAllProducts() {
    LOG.info("Get all products from the db");
    return productservice.getAllProducts();
  }

  @GetMapping("/{id}/{quantity}")
  public ResponseEntity<Product> getProductForCart(@PathVariable("id") String id,
      @PathVariable("quantity") String quantity) {
    LOG.info("Get products from the db : {}", id);
    Product product = productservice.getProductForCart(id, Integer.parseInt(quantity));
    return new ResponseEntity<>(product, HttpStatus.FOUND);
  }

  @GetMapping("/{productId}")
  public ResponseEntity<Product> getProductForOrder(@PathVariable String productId) {
    LOG.info("Retrieving the product from database: {}" , productId);
    return productservice.getProductForOrder(productId);
  }

  @PostMapping("/{id}/{quantity}")
  public ResponseEntity<Product> deleteProductFromCart(@PathVariable("id") String id,
      @PathVariable("quantity") Integer quantity) {
    Product product = productservice.deleteProductFromCart(id, quantity);
    return new ResponseEntity<>(product, HttpStatus.OK);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Product createProduct(@RequestBody Product productRequest) {
    LOG.info("Creating product");
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
