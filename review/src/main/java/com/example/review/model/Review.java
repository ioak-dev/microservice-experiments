package com.example.review.model;

import java.util.Objects;

public class Review {

  private String id;

  private String productId;

  private String userId;

  private String reviewDescription;

  private int ratings;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Review review = (Review) o;
    return id == review.id && Objects.equals(productId, review.productId)
      && Objects.equals(userId, review.userId) && Objects.equals(
      reviewDescription, review.reviewDescription);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, productId, userId, reviewDescription);
  }

  @Override
  public String toString() {
    return "Review{" +
      "id=" + id +
      ", productId='" + productId + '\'' +
      ", userId='" + userId + '\'' +
      ", reviewDescription='" + reviewDescription + '\'' +
      '}';
  }


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getProductId() {
    return productId;
  }

  public void setProductId(String productId) {
    this.productId = productId;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getReviewDescription() {
    return reviewDescription;
  }

  public void setReviewDescription(String reviewDescription) {
    this.reviewDescription = reviewDescription;
  }

  public int getRatings() {
    return ratings;
  }

  public void setRatings(int ratings) {
    this.ratings = ratings;
  }
}
