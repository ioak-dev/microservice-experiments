package com.example.product.service;

import com.example.product.model.Product;
import com.example.product.repository.ProductRepository;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Slf4j
public class ProductService {

  @Autowired
  private ProductRepository productRepo;

  public void createProduct(Product productRequest) {
    Product product = Product
        .builder()
        .name(productRequest.getName())
        .description(productRequest.getDescription())
        .price(productRequest.getPrice())
        .category(productRequest.getCategory())
        .build();

    productRepo.save(product);
    log.info("Product {} is saved !!", product.getId());
  }

  public List<Product> getAllProducts() {
    return productRepo.findAll();
  }

  private Product maptoProductResponse(Product product) {
    return Product.builder()
        .id(product.getId())
        .description(product.getDescription())
        .name(product.getName())
        .price(product.getPrice())
        .category(product.getCategory())
        .build();
  }

  public Product getProductById(String id) {
    Optional<Product> optionalProd = productRepo.findById(id);
    return optionalProd.map(this::maptoProductResponse).orElse(null);
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
}
