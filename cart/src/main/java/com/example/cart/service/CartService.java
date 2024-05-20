package com.example.cart.service;

import com.example.cart.config.UserClient;
import com.example.cart.model.Cart;
import com.example.cart.model.OrderRequest;
import com.example.cart.model.Product;
import com.example.cart.model.User;
import com.example.cart.model.UserCart;
import com.example.cart.orderconfig.OrderClient;

import com.example.cart.productconfig.ProductClient;
import com.example.cart.repository.CartRepository;
import com.example.cart.repository.UserCartRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Slf4j
public class CartService {

  @Autowired
  private CartRepository cartRepository;

  @Autowired
  private OrderClient orderClient;

  @Autowired
  private UserClient userClient;

  @Autowired
  private ProductClient productClient;

  @Autowired
  private UserCartRepository userCartRepository;

  public ResponseEntity<Cart> addProductToCart(String id, String productId, Integer quantity) {
    UserCart userCart = userCartRepository.findById(id).orElseThrow(() ->
        new ResponseStatusException(HttpStatus.NOT_FOUND));
    Cart cart = cartRepository.findByUserCartId(id);
    if(cart==null){
      cart=new Cart();
    }
    List<Product> orderProducts = productClient.getAllProducts();
    Optional<Product> orderProduct = orderProducts.stream()
        .filter(ids -> ids.getId().equals(productId)).findFirst();
    if (!orderProduct.isPresent()) {
       throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    List<Product> orderProductList = new ArrayList<>();
    Cart existingCart= cartRepository.findByUserCartId(userCart.getCartId());
    if(existingCart!=null && existingCart.getProducts()!=null
    && !existingCart.getProducts().isEmpty()){
      orderProductList.addAll(existingCart.getProducts());
    }
    orderProduct.get().setQuantity(quantity);
    orderProductList.add(orderProduct.get());
    cart.setUserId(userCart.getUserId());
    cart.setUserCartId(userCart.getCartId());
    cart.setProducts(orderProductList);
    cartRepository.save(cart);
    return ResponseEntity.status(HttpStatus.CREATED).body(cart);
  }

  public ResponseEntity<Cart> removeProductFromCart(String id, String productId) {
    Cart cart = cartRepository.findByUserCartId(id);
    if (cart != null) {
      List<Product> existingProdList = cart.getProducts();
      List<Product> newProdList = new ArrayList<>();
      for (Product product : existingProdList) {
        if (!product.getId().equals(productId)) {
          newProdList.add(product);
        }
      }
      cart.setProducts(newProdList);
      cartRepository.save(cart);
    }
    return ResponseEntity.status(HttpStatus.OK).body(cart);
  }

  public void orderFromCart(String id, String productId, String userId, OrderRequest orderRequest) {
    Cart cart = cartRepository.findByUserCartId(id);
    if(cart!=null) {
      if (orderClient.OrderProducts(productId,orderRequest, cart.getUserId()).getStatusCode().is2xxSuccessful()) {
        cartRepository.delete(cart);
      }
    }
  }

  public ResponseEntity<List<Product>> getProductsFromCart(String id) {
    Cart cart = cartRepository.findByUserCartId(id);
    if(cart==null){
      throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No products found");
    }
    return ResponseEntity.status(HttpStatus.OK).body(cart.getProducts());
  }

  public void createCartForUser(String userId) {
    User user = userClient.getUser(userId).getBody();
    if (user == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    UserCart userCart = UserCart.builder()
        .userId(userId).build();
    userCartRepository.save(userCart);
  }
}
