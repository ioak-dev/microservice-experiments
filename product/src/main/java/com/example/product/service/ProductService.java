package com.example.product.service;

import com.example.product.controller.ProductController;
import com.example.product.model.Product;
import com.example.product.repository.ProductRepository;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ProductService {

  private static final Logger LOG = LogManager.getLogger(ProductService.class);

  @Autowired
  private ProductRepository productRepo;

  public Product createProduct(Product productRequest) {
    Product product = Product
        .builder()
        .name(productRequest.getName())
        .description(productRequest.getDescription())
        .price(productRequest.getPrice())
        .category(productRequest.getCategory())
        .quantity(productRequest.getQuantity())
        .build();

    Product productResponse = productRepo.save(product);
    LOG.info("Product {} is saved !!", product.getId());
    return productResponse;
  }

  public List<Product> getAllProducts() {
    return productRepo.findAll();
  }

  private Product maptoProductResponse(Product product) {
    LOG.info("Map fields to domain fields");
    return Product.builder()
        .id(product.getId())
        .description(product.getDescription())
        .name(product.getName())
        .price(product.getPrice())
        .category(product.getCategory())
        .quantity(product.getQuantity())
        .build();
  }

  public Product getProductForCart(String id, Integer quantity) {
    Optional<Product> optionalProd = productRepo.findById(id);
    Product product = optionalProd.map(this::maptoProductResponse).orElse(null);
    assert product != null;
    if (product.getQuantity() > quantity) {
      product.setQuantity(product.getQuantity() - quantity);
      productRepo.save(product);
      return product;
    }
    return null;
  }

  public void deleteProduct(String id) {
    if (id != null) {
      productRepo.deleteById(id);
    }
  }

  public Product updateProduct(String id, Product product) {
    if (id != null) {
      Product existingProduct = productRepo.findById(id).orElseThrow(() ->
          new ResponseStatusException(HttpStatus.NOT_FOUND));
      existingProduct.setName(product.getName());
      existingProduct.setDescription(product.getDescription());
      existingProduct.setPrice(product.getPrice());
      existingProduct.setCategory(product.getCategory());
      return productRepo.save(existingProduct);
    }
    return null;
  }

  public void addProductToInventory(String id, Integer quantity) {
    if (id != null) {
      Product existingProduct = productRepo.findById(id).orElseThrow(() ->
          new ResponseStatusException(HttpStatus.NOT_FOUND));
      existingProduct.setQuantity(existingProduct.getQuantity() + quantity);
      productRepo.save(existingProduct);
    }
  }

  public Product deleteProductFromCart(String id, Integer quantity) {
    if (id != null) {
      Product existingProduct = productRepo.findById(id).orElseThrow(() ->
          new ResponseStatusException(HttpStatus.NOT_FOUND));
      existingProduct.setQuantity(existingProduct.getQuantity() + quantity);
      return productRepo.save(existingProduct);
    }
    return null;
  }

  public ResponseEntity<Product> getProductForOrder(String id) {
    if (id != null) {
      Product productTemp = productRepo.findById(id).orElseThrow();
      return new ResponseEntity<>(productTemp, HttpStatus.OK);
    }
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }
}
