package com.ride.Goober.subscribers;

import com.ride.Goober.event.PublisherEvents.DriverFoundEvent;
import com.ride.Goober.event.PublisherEvents.DriverNotFoundEvent;
import com.ride.Goober.event.RideRequestEvent;
import io.awspring.cloud.sqs.annotation.SqsListener;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DriverMatchingService {

  @Autowired
  private SqsTemplate sqsTemplate;

  @Value("${aws.sns.topic.arn}")
  private String snsTopicArn;


  @SqsListener("${aws.sqs.driver.queue}")
  public void handleRideRequest(RideRequestEvent rideRequestEvent) {
    log.info("Driver Matching Service: Assigning driver for request: {}", rideRequestEvent);
    boolean isDriverAssigned=false;
    if (isDriverAssigned) {
      log.info("Driver assigned successfully for request: {}", rideRequestEvent);
      sqsTemplate.send(sqsSendOptions -> sqsSendOptions
          .queue("notificationQueue")
          .payload(new DriverFoundEvent(rideRequestEvent.getUserId(), "Driver assigned",
              rideRequestEvent.getPickupLocation(),rideRequestEvent.getDropOffLocation())));
    } else {
      log.warn("Driver not found for request: {}", rideRequestEvent);
      sqsTemplate.send(sqsSendOptions -> sqsSendOptions
          .queue("notificationQueue")
          .payload(new DriverNotFoundEvent(rideRequestEvent.getUserId(), "Driver not found")));
    }
  }
}
