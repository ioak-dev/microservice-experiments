package com.hexagonal.user.domain.model;

import lombok.Getter;

@Getter
public class Events {
  private final String message;

  public Events(String message) {
    this.message = message;
  }

}
