package com.ride.Goober.event;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RideRequestEvent {


  private String userId;
  private String pickupLocation;
  private String dropOffLocation;
  private String rideType;

}
