package com.hexagonal.user.adapters.events;

import com.hexagonal.user.application.port.EventListenerUser;

import com.hexagonal.user.domain.model.Events;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class SpringEventListener implements EventListenerUser {

  @EventListener
  @Override
  public void onEvent(Events event) {
    System.out.println("Received event: " + event.getMessage());
  }
}
