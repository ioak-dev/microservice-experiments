package com.ride.Goober.publisher;

import com.ride.Goober.event.RideRequestEvent;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RideRequestPublisher {

  @Autowired
  private SqsTemplate sqsTemplate;

  @Value("${aws.sns.topic.arn}")
  private String snsTopicArn;

  public void publishRideRequest(RideRequestEvent rideRequestEvent) {
   /* sqsTemplate
        .send(sqsSendOptions ->
            sqsSendOptions
                .queue("NotificationQueue")
                .payload(rideRequestEvent)
        );
    sqsTemplate
        .send(sqsSendOptions ->
            sqsSendOptions
                .queue("driverqueue")
                .payload(rideRequestEvent)
        );
    sqsTemplate
        .send(sqsSendOptions ->
            sqsSendOptions
                .queue("pricingQueue")
                .payload(rideRequestEvent)
        );*/
    sqsTemplate.send(sqsSendOptions -> sqsSendOptions
        .queue("driverqueue")
        .payload(rideRequestEvent));
    log.info("Message published to SNS: " + rideRequestEvent);

  }

  public void publishRideRequest(List<RideRequestEvent> events) {
    List<String> queues = List.of("NotificationQueue", "driverQueue", "pricingQueue");

    for (String queue : queues) {
      publishEventsToQueue(events, queue);
    }

    log.info("Messages published to queues: {}", queues);
  }

  private void publishEventsToQueue(List<RideRequestEvent> events, String queue) {
    for (RideRequestEvent event : events) {
      sqsTemplate.send(sqsSendOptions ->
          sqsSendOptions
              .queue(queue)
              .payload(event)
      );
    }
    log.info("Published {} events to {}", events.size(), queue);
  }

}
