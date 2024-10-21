package com.hexagonal.user.adapters.events;

import com.hexagonal.user.application.port.EventPublisher;
import com.hexagonal.user.domain.model.Events;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class SpringEventPublisher implements EventPublisher {

  private final ApplicationEventPublisher applicationEventPublisher;

  public SpringEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
    this.applicationEventPublisher = applicationEventPublisher;
  }

  @Override
  public void publish(Events event) {
    applicationEventPublisher.publishEvent(event);
  }
}
