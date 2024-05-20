package com.example.cart.repository;

import com.example.cart.model.UserCart;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserCartRepository extends MongoRepository<UserCart,String> {

}
