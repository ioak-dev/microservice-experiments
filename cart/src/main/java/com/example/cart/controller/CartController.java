package com.example.cart.controller;

import com.example.cart.model.Cart;
import com.example.cart.model.OrderRequest;
import com.example.cart.model.Product;
import com.example.cart.repository.CartRepository;
import com.example.cart.service.CartService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.annotation.PostConstruct;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
public class CartController {

  private static final Logger LOG = LogManager.getLogger(CartController.class);

  private List<Cart> cartList=new ArrayList<>();

  @Autowired
  private CartRepository cartRepository;

  @PostConstruct
  public void addProductToList(){
    cartList.addAll(cartRepository.findAll());
  }

  @Autowired
  private CartService cartService;

  @PostMapping("/{id}/add/{productId}/{quantity}")
  public ResponseEntity<Cart> addProductToCart(@PathVariable("id") String id,
      @PathVariable("productId") String productId, @PathVariable("quantity") Integer quantity) {
    LOG.info("Add the product:{} to cart :{}", productId,id);
    return cartService.addProductToCart(id,productId, quantity);
  }

  @PostMapping("/{id}/remove/{productId}")
  public ResponseEntity<Cart> removeProductFromCart(@PathVariable("id") String id,
      @PathVariable("productId") String productId) {
    LOG.info("Remove the product:{} from cart :{}", productId,id);
    return cartService.removeProductFromCart(id,productId);
  }

  @PostMapping("/{id}/order/{userId}/{prodId}")
  public void orderFromCart(@PathVariable("id") String id, @PathVariable("userId") String userId
  ,@PathVariable("prodId") String prodId,@RequestBody OrderRequest orderRequest) {
    LOG.info("Order the item : {} ,from cart : {}",prodId,id);
    cartService.orderFromCart(id, userId,prodId, orderRequest);
  }

  @GetMapping("/{id}")
  @CircuitBreaker(name = "cartService", fallbackMethod = "displayProductsFromCart")
  public ResponseEntity<List<Product>> getProductsFromCart(@PathVariable("id") String id) {
    LOG.info("Retrieve all the products from cart : {}", id);
    return cartService.getProductsFromCart(id);
  }

  public ResponseEntity<List<Product>> displayProductsFromCart(String id,Exception e){
    List<Product> productList= new ArrayList<>();
    System.out.println(cartList);
    cartList.stream().filter(cart -> cart.getId().equals(id))
        .forEach(products->
        productList.addAll(products.getProducts()));
    return ResponseEntity.ok(productList);
  }

  @PostMapping("/{userId}")
  public void createCartForUserId(@PathVariable String userId){
    LOG.info("Creating cart for user : {}",userId);
    cartService.createCartForUser(userId);
  }

  @GetMapping("/hello")
  public String testDocker(){
    return "Hello Docker " + ZonedDateTime.now();
  }
}
