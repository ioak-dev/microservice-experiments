package com.ride.Goober.subscribers;

import com.ride.Goober.event.PublisherEvents.DriverFoundEvent;
import com.ride.Goober.event.PublisherEvents.DriverNotFoundEvent;
import com.ride.Goober.event.RideRequestEvent;
import com.ride.Goober.model.Price;
import io.awspring.cloud.sqs.annotation.SqsListener;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.server.ResponseStatusException;

@Service
@Slf4j
public class NotificationService {

  @Value("${mail.imaps.username}")
  private String username;

  @Value("${mail.imaps.password}")
  private String password;

  @Autowired
  private PricingService pricingService;

  @Autowired
  private SqsTemplate sqsTemplate;

  @Autowired
  private StringRedisTemplate redisTemplate;

  @Value("${event.deduplication.ttl}")
  private long deduplicationTtl;

  @SqsListener("${aws.sqs.notification.queue}")
  public void sendNotifications(Object notificationEvent) {
    String eventId = extractEventId(notificationEvent);

    if (isDuplicate(eventId)) {
      log.info("Duplicate notification event detected: {}", eventId);
      return;
    }
    try {
      if (notificationEvent instanceof DriverFoundEvent) {
        DriverFoundEvent event = (DriverFoundEvent) notificationEvent;
        sendMailTemplate(event.getUserId(), new RideRequestEvent(
            event.getUserId(),
            event.getPickup(),
            event.getDropOff(),
            null,
            eventId));
        sqsTemplate.send(sqsSendOptions -> sqsSendOptions
            .queue("pricingQueue")
            .payload(new RideRequestEvent(
                event.getUserId(),
                event.getPickup(),
                event.getDropOff(),
                null,
                eventId)));
      } else if (notificationEvent instanceof DriverNotFoundEvent) {
        DriverNotFoundEvent event = (DriverNotFoundEvent) notificationEvent;
        sendMailTemplate(event.getUserId(), null);
      }
    } finally {
      markEventProcessed(eventId);
    }
  }

  private String extractEventId(Object event) {
    if (event instanceof DriverFoundEvent) {
      return ((DriverFoundEvent) event).getEventId();
    } else if (event instanceof DriverNotFoundEvent) {
      return ((DriverNotFoundEvent) event).getEventId();
    }
    return null;
  }

  private boolean isDuplicate(String eventId) {
    String redisKey = "notificationQueue:" + eventId;
    Boolean exists = redisTemplate.hasKey(redisKey);
    return exists != null && exists;
  }

  private void markEventProcessed(String eventId) {
    String redisKey = "notificationQueue:" + eventId;
    redisTemplate.opsForValue().set(redisKey, "processed", Duration.ofSeconds(deduplicationTtl));
  }


  public Session configureProperties() {
    String host = "smtp.gmail.com";
    Properties props = new Properties();
    props.put("mail.smtp.host", host);
    props.put("mail.smtp.port", "587");
    props.put("mail.transport.protocol", "smtp");
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.starttls.required", "true");
    Authenticator authenticator = new Authenticator() {
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
      }
    };
    Session session = Session.getInstance(props, authenticator);
    return session;
  }

  public void sendMailTemplate(String to,RideRequestEvent rideRequestEvent) {
    try {
      Session session = configureProperties();
      Message message = new MimeMessage(session);
      message.setFrom(new InternetAddress(username));
      message.setRecipients(Message.RecipientType.TO,
          InternetAddress.parse(to));
      message.setSubject("Email Received!!!");
      if (rideRequestEvent!=null) {
        Resource resource = new ClassPathResource("templates/email-template.txt");
        String template = StreamUtils.copyToString(resource.getInputStream(),
            StandardCharsets.UTF_8);
        Price price = pricingService.handlePricing(rideRequestEvent);
        message.setText(
            template.replace("{price}", price.getPrice()).replace("{total}", price.getDistance()));
        Transport.send(message);
      }else {
        message.setText("Ride cancelled");
        Transport.send(message);
      }
      log.info("Email Message Sent Successfully");
    } catch (MessagingException e) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
