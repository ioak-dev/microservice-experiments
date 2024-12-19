package com.ride.Goober.subscribers;

import com.ride.Goober.event.PublisherEvents.DriverFoundEvent;
import com.ride.Goober.event.PublisherEvents.DriverNotFoundEvent;
import com.ride.Goober.event.RideRequestEvent;
import io.awspring.cloud.sqs.annotation.SqsListener;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@Slf4j
public class DriverMatchingService {

  @Autowired
  private SqsTemplate sqsTemplate;

  @Autowired
  private StringRedisTemplate redisTemplate;

  @Value("${aws.sns.topic.arn}")
  private String snsTopicArn;

  @Value("${event.deduplication.ttl}")
  private long deduplicationTtl;

  @SqsListener("${aws.sqs.driver.queue}")
  public void handleRideRequest(RideRequestEvent rideRequestEvent) {
    if (isDuplicate(rideRequestEvent.getEventId())) {
      log.info("Duplicate event detected: {}", rideRequestEvent.getEventId());
      return;
    }

    log.info("Processing ride request: {}", rideRequestEvent);

    try {
      boolean isDriverAssigned = assignDriver(rideRequestEvent);
      if (isDriverAssigned) {
        log.info("Driver assigned successfully for request: {}", rideRequestEvent);
        sqsTemplate.send(sqsSendOptions -> sqsSendOptions
            .queue("notificationQueue")
            .payload(new DriverFoundEvent(
                rideRequestEvent.getUserId(),
                "Driver assigned",
                rideRequestEvent.getPickupLocation(),
                rideRequestEvent.getDropOffLocation(),
                rideRequestEvent.getEventId())));
      } else {
        log.warn("Driver not found for request: {}", rideRequestEvent);
        sqsTemplate.send(sqsSendOptions -> sqsSendOptions
            .queue("notificationQueue")
            .payload(new DriverNotFoundEvent(
                rideRequestEvent.getUserId(),
                "Driver not found",
                rideRequestEvent.getEventId())));
      }
    } catch (Exception e) {
      log.error("Error processing ride request: {}", rideRequestEvent, e);
    } finally {
      markEventProcessed(rideRequestEvent.getEventId());
    }
  }

  private boolean isDuplicate(String eventId) {
    String redisKey = "driverQueue:" + eventId;
    Boolean exists = redisTemplate.hasKey(redisKey);
    return exists != null && exists;
  }

  private void markEventProcessed(String eventId) {
    String redisKey = "driverQueue:" + eventId;
    redisTemplate.opsForValue().set(redisKey, "processed", Duration.ofSeconds(deduplicationTtl));
  }

  private boolean assignDriver(RideRequestEvent rideRequestEvent) {
    return Math.random() > 0.5;
  }
}
