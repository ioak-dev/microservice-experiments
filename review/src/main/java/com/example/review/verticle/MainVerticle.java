package com.example.review.verticle;

import com.example.review.controller.ReviewController;
import com.example.review.model.Response;
import com.example.review.model.Review;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public class MainVerticle extends AbstractVerticle {

  private static final Logger LOGGER = LoggerFactory.getLogger(MainVerticle.class);

  private static Router router;
  private ReviewController reviewController;

  private static final String CONTENT_TYPE = "application/json";

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    reviewController = new ReviewController(vertx);
    router = Router.router(vertx);
    router.route().handler(BodyHandler.create(false));

    router.post("/api/review/add").produces(CONTENT_TYPE)
      .handler(routingContext -> {
        JsonObject jsonObject = new JsonObject();
        Review user = Json.decodeValue(routingContext.body().buffer(), Review.class);
        reviewController.insertReview(user).future()
          .onSuccess(response -> {
            jsonObject.put("message", "record added");
            routingContext.response()
              .setStatusCode(200)
              .putHeader("content-type", CONTENT_TYPE)
              .end(jsonObject.encode());
          });
        routingContext.response().putHeader("content-type", CONTENT_TYPE)
          .end(jsonObject.encode());
      });

    router.get("/api/reviews/getall").produces(CONTENT_TYPE)
      .handler(routingContext -> {
        reviewController.getAllReviews().future().onSuccess(resp -> response(routingContext, resp));
      });

    router.put("/api/review/update/:id").produces(CONTENT_TYPE)
      .handler(routingContext->{
        String id=routingContext.request().getParam("id");
        Review review = Json.decodeValue(routingContext.body().buffer(), Review.class);
        JsonObject jsonObject=new JsonObject();
        reviewController.updateReview(id,review).future()
          .onSuccess(response -> {
            jsonObject.put("message", "record added");
            routingContext.response()
              .setStatusCode(200)
              .putHeader("content-type", CONTENT_TYPE)
              .end(jsonObject.encode());
          });
        routingContext.response().putHeader("content-type", CONTENT_TYPE)
          .end(jsonObject.encode());
      });

    HttpServerOptions options = new HttpServerOptions().setTcpKeepAlive(true);
    vertx.createHttpServer(options)
      .requestHandler(router)
      .listen(8082)
      .onSuccess(httpServer -> {
        LOGGER.info("Server started on port 8082");
        startPromise.tryComplete();
      })
      .onFailure(startPromise::tryFail);
  }

  private void response(RoutingContext routingContext, Response response) {
    response.getHeaders().stream()
      .forEach(entry ->
        routingContext.response().putHeader(entry.getKey(), entry.getValue().toString()));
    routingContext.response().setStatusCode(response.getStatusCode())
      .end(response.getResponseBody());
  }
}
