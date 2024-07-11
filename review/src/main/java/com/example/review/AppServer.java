package com.example.review;

import com.example.review.verticle.MainVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;


public class AppServer {
  private static final Logger LOGGER = LoggerFactory.getLogger(AppServer.class);
  public static void main(String[] args) {
    Vertx vertx= Vertx.vertx();
    vertx.deployVerticle(MainVerticle.class.getName())
      .onFailure(throwable ->
      LOGGER.error("Error while starting Main verticle", throwable));
//    vertx.deployVerticle(EurekaVerticle.class.getName())
//      .onFailure(throwable->LOGGER.error("Error while starting eureka",throwable));
  }

}
