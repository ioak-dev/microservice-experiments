package com.hexagonal.user.application.port;


import com.hexagonal.user.domain.model.Events;

public interface EventPublisher {
  void publish(Events event);
}
