package com.hexagonal.user.application.port;


import com.hexagonal.user.domain.model.Events;

public interface EventListenerUser {
  void onEvent(Events event);
}
