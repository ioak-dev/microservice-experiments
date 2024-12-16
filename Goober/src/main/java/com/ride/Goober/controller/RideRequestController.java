package com.ride.Goober.controller;

import com.ride.Goober.event.RideRequestEvent;
import com.ride.Goober.publisher.RideRequestPublisher;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/event")
@RequiredArgsConstructor
public class RideRequestController {

  private final RideRequestPublisher rideRequestPublisher;

  @PostMapping
  public String createRideRequest(@RequestBody RideRequestEvent rideRequestEvent) {
    rideRequestPublisher.publishRideRequest(rideRequestEvent);
    return "Ride request published";
  }

  @PostMapping("/single")
  public String createSingleRideRequest(@RequestBody RideRequestEvent rideRequestEvent) {
    rideRequestPublisher.publishRideRequest(List.of(rideRequestEvent));
    return "Single ride request published.";
  }

  @PostMapping("/batch")
  public String createBatchRideRequest(@RequestBody List<RideRequestEvent> rideRequestEvents) {
    rideRequestPublisher.publishRideRequest(rideRequestEvents);
    return "Batch ride requests published.";
  }
}
