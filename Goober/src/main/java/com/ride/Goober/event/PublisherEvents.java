package com.ride.Goober.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PublisherEvents {

  @Data
  @AllArgsConstructor
  public static class RideRequestEvent {
    private String rideId;
    private String userId;
  }

  @Data
  @AllArgsConstructor
  public static class DriverFoundEvent {
    private String userId;
    private String status;

    private String pickup;

    private String dropOff;
  }

  @Data
  @AllArgsConstructor
  public static class DriverNotFoundEvent {
    private String userId;
    private String status;
  }

  @Data
  @AllArgsConstructor
  public static class PriceCalculationRequest {
    private String rideId;
  }

  @Data
  @AllArgsConstructor
  public static class RideCompletedEvent {
    private String rideId;
    private double fare;
  }


}
