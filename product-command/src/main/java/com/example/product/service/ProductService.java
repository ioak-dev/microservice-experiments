package com.example.product.service;

import com.example.product.model.Product;
import com.example.product.dto.ProductEvent;
import com.example.product.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Slf4j
public class ProductService {

  private static final String TOPIC = "product-topic";

  @Autowired
  private ProductRepository productRepo;

  private final KafkaTemplate<String, Object> kafkaTemplate;

  public ProductService(KafkaTemplate<String, Object> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }


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
    log.info("Product {} is saved !!", product.getId());
    ProductEvent productEvent = new ProductEvent("create-product", productResponse);
    kafkaTemplate.send(TOPIC, productEvent);
    return productResponse;
  }

  public void deleteProduct(String id) {
    Product productResponse = productRepo.findById(id).orElseThrow();
    productRepo.deleteById(id);
    ProductEvent productEvent = new ProductEvent("delete-product", productResponse);
    kafkaTemplate.send(TOPIC, productEvent);
  }

  public Product updateProduct(String id, Product product) {
    if (id != null) {
      Product existingProduct = productRepo.findById(id).orElseThrow(() ->
          new ResponseStatusException(HttpStatus.NOT_FOUND));
      existingProduct.setName(product.getName());
      existingProduct.setDescription(product.getDescription());
      existingProduct.setPrice(product.getPrice());
      existingProduct.setCategory(product.getCategory());
      productRepo.save(existingProduct);
      ProductEvent productEvent = new ProductEvent("update-product", existingProduct);
      kafkaTemplate.send(TOPIC, productEvent);
      return existingProduct;
    }
    return null;
  }

  public void addProductToInventory(String id, Integer quantity) {
    if (id != null) {
      Product existingProduct = productRepo.findById(id).orElseThrow(() ->
          new ResponseStatusException(HttpStatus.NOT_FOUND));
      existingProduct.setQuantity(existingProduct.getQuantity() + quantity);
      productRepo.save(existingProduct);
      ProductEvent productEvent = new ProductEvent("addToInventory-product", existingProduct);
      kafkaTemplate.send(TOPIC, productEvent);
    }
  }

  public Product deleteProductFromCart(String id, Integer quantity) {
    if (id != null) {
      Product existingProduct = productRepo.findById(id).orElseThrow(() ->
          new ResponseStatusException(HttpStatus.NOT_FOUND));
      existingProduct.setQuantity(existingProduct.getQuantity() + quantity);
      productRepo.save(existingProduct);
      ProductEvent productEvent = new ProductEvent("deleteFromCart-product", existingProduct);
      kafkaTemplate.send(TOPIC, productEvent);
      return existingProduct;
    }
    return null;
  }
}