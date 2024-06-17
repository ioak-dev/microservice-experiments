package com.example.product.service;

import com.example.product.model.Product;
import com.example.product.dto.ProductEvent;
import com.example.product.repository.ProductRepository;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProductService {

  private static final String TOPIC = "product-topic";

  @Autowired
  private ProductRepository productRepo;

  public List<Product> getAllProducts() {
    return productRepo.findAll();
  }

  private Product mapToProductResponse(Product product) {
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
    if (optionalProd.isEmpty()) {
      log.error("Product with ID {} not found", id);
      return null;
    }

    Product product = mapToProductResponse(optionalProd.get());
    if (product.getQuantity() > quantity) {
      product.setQuantity(product.getQuantity() - quantity);
      productRepo.save(product);
      return product;
    }
    log.warn("Requested quantity {} exceeds available stock for product ID {}", quantity, id);
    return null;
  }

  public ResponseEntity<Product> getProductForOrder(String id) {
    if (id != null) {
      return productRepo.findById(id)
          .map(product -> new ResponseEntity<>(product, HttpStatus.OK))
          .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  @KafkaListener(topics = TOPIC,groupId = "product-event-group")
  public void processProductEvents(ProductEvent productEvent) {
    Product product = productEvent.getProduct();
    String eventType = productEvent.getEventType();

    switch (eventType) {
      case "create-product":
        productRepo.save(product);
        log.info("Created product: {}", product);
        break;

      case "update-product":
        productRepo.findById(product.getId())
            .ifPresentOrElse(existingProduct -> {
              existingProduct.setName(product.getName());
              existingProduct.setDescription(product.getDescription());
              existingProduct.setPrice(product.getPrice());
              existingProduct.setCategory(product.getCategory());
              productRepo.save(existingProduct);
              log.info("Updated product: {}", existingProduct);
            }, () -> {
              log.error("Product with ID {} not found for update", product.getId());
            });
        break;

      case "delete-product":
        productRepo.findById(product.getId())
            .ifPresentOrElse(existingProduct -> {
              productRepo.delete(existingProduct);
            log.info("Deleted product: {}", existingProduct);
            },()->{
              log.error("Product with ID {} not found for delete", product.getId());
            });
        break;

      case "addToInventory-product":
        productRepo.findById(product.getId())
            .ifPresentOrElse(existingProduct -> {
              existingProduct.setQuantity(existingProduct.getQuantity() + product.getQuantity());
              productRepo.save(existingProduct);
              log.info("Added to inventory: {}", existingProduct);
            }, () -> {
              log.error("Product with ID {} not found for adding to inventory", product.getId());
            });
        break;

      case "removeFromInventory-product":
        productRepo.findById(product.getId())
            .ifPresentOrElse(existingProduct -> {
              existingProduct.setQuantity(existingProduct.getQuantity() - product.getQuantity());
              productRepo.save(existingProduct);
              log.info("Removed from inventory: {}", existingProduct);
            }, () -> {
              log.error("Product with ID {} not found for removing from inventory", product.getId());
            });
        break;

      default:
        log.error("Unknown event type: {}", eventType);
    }
  }
}
