package com.example.review.dao;

import com.example.review.model.Response;
import com.example.review.model.Review;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.Tuple;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.UUID;

public class ReviewDao {

  private static final Logger LOGGER = LoggerFactory.getLogger(ReviewDao.class);
  private static final ResourceBundle queriesBundle = ResourceBundle.getBundle("queries");
  private static final ResourceBundle propertiesBundle = ResourceBundle.getBundle("application");
  private MySQLPool pool;

  Vertx vertx = Vertx.vertx();
  WebClientOptions options = new WebClientOptions()
    .setDefaultHost("localhost")
    .setDefaultPort(8060);
  WebClient client = WebClient.create(vertx, options);

  public ReviewDao(Vertx vertx) {
    pool = MySQLPool.pool(vertx, getSqlConnectionOptions(), getPoolOptions());
  }

  public Future<List<Review>> getAllReviews() {
    Promise<List<Review>> promise = Promise.promise();
    List<Review> reviews = new ArrayList<>();
    vertx.setTimer(60000, id -> promise.complete());
    pool.preparedQuery(queriesBundle.getString("GETALL"))
      .execute().onSuccess(rows -> {
        for (Row row : rows) {
          Review review = new Review();
          review.setReviewDescription(row.getString("review_description"));
          review.setRatings(row.getInteger("ratings"));
          review.setProductId(row.getString("product_id"));
          review.setUserId(row.getString("user_id"));
          review.setId(row.getString("id"));
          reviews.add(review);
        }
        promise.tryComplete(reviews);
      });
    return promise.future();
  }

  public Future<List<Review>> updateReview(String id, Review review) {
    LOGGER.info("Updating the review for the product : {}"+review.getProductId());
    Promise<List<Review>> promise = Promise.promise();
    List<Review> reviews = new ArrayList<>();
    pool.preparedQuery(queriesBundle.getString("UPDATE_REVIEW"))
      .execute(Tuple.of(review.getReviewDescription(), review.getRatings(), review.getProductId(),
        review.getUserId(), id))
      .onSuccess(rows -> {
        for (Row row : rows) {
          Review updatedReview = new Review();
          updatedReview.setId(id);
          updatedReview.setReviewDescription(row.getString("reviewDescription"));
          updatedReview.setRatings(row.getInteger("ratings"));
          updatedReview.setProductId(row.getString("productId"));
          updatedReview.setUserId(row.getString("userId"));
          reviews.add(updatedReview);
          LOGGER.info(updatedReview);
        }
        LOGGER.info(reviews);
        promise.tryComplete(reviews);
      }).onFailure(throwable -> {
        promise.fail(throwable); // Forward the failure
      });
    return promise.future();
  }

  public Future<List<Review>> insertReview(Review reviewRequest) {
    LOGGER.info("Insert review for product : {}" + reviewRequest.getProductId());
    Promise<List<Review>> promise = Promise.promise();
    List<Review> reviews = new ArrayList<>();
    String userId = reviewRequest.getUserId();
    String productId = reviewRequest.getProductId();
    Promise<Void> userPromise = Promise.promise();
    Promise<Void> productPromise = Promise.promise();
    vertx.setTimer(60000, id -> promise.complete());
    checkUserExists(userId, userPromise);
    checkProductExists(productId, productPromise);
    CompositeFuture.all(userPromise.future(), productPromise.future()).onComplete(ar -> {
      if (ar.succeeded()) {
        pool.preparedQuery(queriesBundle.getString("INERT_REVIEW"))
          .execute(
            Tuple.of(UUID.randomUUID(), reviewRequest.getProductId(), reviewRequest.getUserId(),
              reviewRequest.getReviewDescription(), reviewRequest.getRatings())).onSuccess(rows -> {
            for (Row row : rows) {
              Review review = new Review();
              review.setId(row.getString("id"));
              review.setReviewDescription(row.getString("review_description"));
              review.setRatings(row.getInteger("ratings"));
              review.setProductId(row.getString("product_id"));
              review.setUserId(row.getString("user_id"));
              reviews.add(reviewRequest);
              LOGGER.info(reviews);
            }
            promise.tryComplete(reviews);
          }).onFailure(Throwable::printStackTrace);
      }
    });
    return promise.future();
  }

  private void checkUserExists(String userId, Promise<Void> userPromise) {
    client.get("/api/user/" + userId)
      .send(ar -> {
        if (ar.succeeded()) {
          HttpResponse<Buffer> response = ar.result();
          LOGGER.info("Got HTTP response with status " + response.statusCode());
          LOGGER.info("Response body: " + response.bodyAsString());
          if (response.statusCode() != 200) {
            userPromise.fail("User not found");
          } else {
            userPromise.complete();
          }
        } else {
          userPromise.fail("Not possible to connect to service");
        }
      });
  }

  private void checkProductExists(String productId, Promise<Void> productPromise) {
    client.get("/api/product/" + productId)
      .send(ar -> {
        if (ar.succeeded()) {
          HttpResponse<Buffer> response = ar.result();
          LOGGER.info("Got HTTP response with status " + response.statusCode());
          LOGGER.info("Response body: " + response.bodyAsString());
          if (response.statusCode() != 200) {
            productPromise.fail("User not found");
          } else {
            productPromise.complete();
          }
        } else {
          productPromise.fail("Not possible to connect to service");
        }
      });
  }

  private MySQLConnectOptions getSqlConnectionOptions() {
    LOGGER.info("Creating connection pool");
    MySQLConnectOptions connectOptions = new MySQLConnectOptions()
      .setHost("localhost")
      .setDatabase(propertiesBundle.getString("app.database"))
      .setUser(propertiesBundle.getString("app.username"))
      .setPassword(propertiesBundle.getString("app.password"));
    return connectOptions;
  }

  private PoolOptions getPoolOptions() {
    return new PoolOptions()
      .setMaxSize(Integer.parseInt(propertiesBundle.getString("app.database.pool-size")));
  }

}
