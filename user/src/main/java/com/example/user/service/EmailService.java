package com.example.user.service;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

  @Value("${mail.imaps.username}")
  private String userName;


  @Value("${mail.imaps.password}")
  private String password;

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
        return new PasswordAuthentication(userName, password);
      }
    };
    Session session = Session.getInstance(props, authenticator);
    return session;
  }

  public void sendEmail(String email, String subject, String body) {
    try {
      Session session = configureProperties();
      Message message = new MimeMessage(session);
      message.setFrom(new InternetAddress(userName));
      message.setRecipients(Message.RecipientType.TO,
          InternetAddress.parse(email));
      message.setSubject(subject);
      message.setText(body);
      Transport.send(message);
    } catch (MessagingException e) {
     e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
