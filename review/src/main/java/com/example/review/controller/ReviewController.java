package com.example.review.controller;

import com.example.review.dao.ReviewDao;
import com.example.review.model.Response;
import com.example.review.model.Review;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ReviewController {

  private static final Logger LOG = LogManager.getLogger(ReviewController.class);
  private final ReviewDao reviewDao;

  public ReviewController(Vertx vertx) {
    reviewDao = new ReviewDao(vertx);
  }

  public Promise<Response> getAllReviews() {
    Promise<Response> promise = Promise.promise();
    LOG.info("Get all reviews from the database");
    Response response = new Response();
    reviewDao.getAllReviews()
      .onSuccess(reviews -> {
        response.setStatusCode(200)
          .setResponseBody(Json.encode(reviews));
        promise.tryComplete(response);
      }).onFailure(throwable -> response.setStatusCode(500));
    return promise;
  }

  public Promise<Response> insertReview(Review review) {
    Promise<Response> responsePromise = Promise.promise();
    Response response = new Response();
    reviewDao.insertReview(review).onSuccess(reviews -> {
      response.setStatusCode(200)
        .setResponseBody(Json.encode(reviews));
      responsePromise.tryComplete(response);
    }).onFailure(throwable -> response.setStatusCode(500)
      .setResponseBody("Failed to insert!!"));
    return responsePromise;
  }
  public Promise<Response> updateReview(String id,Review review){
    Promise<Response> responsePromise=Promise.promise();
    Response response = new Response();
    reviewDao.updateReview(id, review).onSuccess(reviews -> {
      response.setStatusCode(200)
        .setResponseBody(Json.encode(reviews));
      responsePromise.tryComplete(response);
    }).onFailure(throwable -> response.setStatusCode(500)
      .setResponseBody("Failed to update!!"));
    return responsePromise;
  }
}
