package com.ride.Goober.subscribers;

import com.ride.Goober.event.PublisherEvents.RideCompletedEvent;
import com.ride.Goober.event.RideRequestEvent;
import com.ride.Goober.model.Price;
import io.awspring.cloud.sqs.annotation.SqsListener;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PricingService {


  @Autowired
  private SqsTemplate sqsTemplate;


  @SqsListener("${aws.sqs.pricing.queue}")
  public Price handlePricing(RideRequestEvent rideRequestEvent) {
    log.info("Pricing Service: Calculating price for request: {}", rideRequestEvent);
    try {
      double[] coords1 = getCoordinates(rideRequestEvent.getPickupLocation());
      if (coords1 == null) {
        log.info("Failed to fetch coordinates for {}",rideRequestEvent.getPickupLocation());
        return null;
      }
      double[] coords2 = getCoordinates(rideRequestEvent.getDropOffLocation());
      if (coords2 == null) {
        log.info("Failed to fetch coordinates for {}",rideRequestEvent.getDropOffLocation());
        return null;
      }
      double distance = calculateDistance(coords1[0], coords1[1], coords2[0], coords2[1]);
      log.info("Distance: " + distance + " km");
      double price = (distance * 30) + 100;
      log.info("Price: " + price);

      sqsTemplate.send(sqsSendOptions -> sqsSendOptions
          .queue("completionQueue")
          .payload(new RideCompletedEvent(rideRequestEvent.getUserId(), price)));

      return Price.builder().price(String.valueOf(price)).distance(String.valueOf(distance)).build();
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }

  private static final double EARTH_RADIUS_KM = 6371.0; // Earth's radius in kilometers

  public static double[] getCoordinates(String location) {
    try {
      String urlString = "https://nominatim.openstreetmap.org/search?q=LOCATION&format=json&limit=1";
      URL url = new URL(urlString.replace("LOCATION", location.trim().replace(" ", "%20")));
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");
      connection.setRequestProperty("User-Agent", "Mozilla/5.0");

      BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      StringBuilder response = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) {
        response.append(line);
      }
      reader.close();
      JSONArray jsonArray = new JSONArray(response.toString());
      if (jsonArray.length() > 0) {
        JSONObject locationData = jsonArray.getJSONObject(0);
        double lat = locationData.getDouble("lat");
        double lon = locationData.getDouble("lon");
        return new double[]{lat, lon};
      } else {
        log.info("No results found for location: " + location);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null; 
  }

  public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
    double lat1Rad = Math.toRadians(lat1);
    double lon1Rad = Math.toRadians(lon1);
    double lat2Rad = Math.toRadians(lat2);
    double lon2Rad = Math.toRadians(lon2);

    double deltaLat = lat2Rad - lat1Rad;
    double deltaLon = lon2Rad - lon1Rad;

    double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
        Math.cos(lat1Rad) * Math.cos(lat2Rad) *
            Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);

    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

    return EARTH_RADIUS_KM * c;
  }

}
