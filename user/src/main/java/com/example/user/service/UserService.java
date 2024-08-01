package com.example.user.service;

import com.example.user.model.RegisterRequest;
import com.example.user.model.RegisterResponse;
import com.example.user.model.User;
import com.example.user.repository.UserRepository;
import java.util.Random;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {

  private static final Logger LOG = LogManager.getLogger(UserService.class);

  @Autowired
  private UserRepository userRepository;


  @Autowired
  private EmailService emailService;


  public User saveUser(User user) {
    LOG.info("Saving the user details to db");
    return userRepository.save(user);
  }

  public ResponseEntity<User> getUserById(String id) {
    User user = userRepository.findById(id).orElseThrow(() ->
        new ResponseStatusException(HttpStatus.NOT_FOUND));
    return new ResponseEntity<>(user, HttpStatus.OK);
  }

  public void deleterById(String id) {
    userRepository.deleteById(id);
  }

  public User updateUser(String id, User user) {
    LOG.info("Updating the user details");
    if (id != null) {
      User existingUser = userRepository.findById(id)
          .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
      existingUser.setName(user.getName());
      existingUser.setAddress(user.getAddress());
      existingUser.setPhoneNumber(user.getPhoneNumber());
      return userRepository.save(existingUser);
    }
    return null;
  }


  public RegisterResponse register(RegisterRequest registerRequest) {
    User existingUser = userRepository.findByEmail(registerRequest.getEmail());
    if (existingUser != null && existingUser.isVerified()) {
      throw new RuntimeException("User Already Registered");
    }
    User users = User.builder()
        .name(registerRequest.getUserName())
        .email(registerRequest.getEmail())
        .password(registerRequest.getPassword())
        .build();
    String otp = generateOTP();
    users.setOtp(otp);

    User savedUser = userRepository.save(users);
    sendVerificationEmail(savedUser.getEmail(), otp);

    RegisterResponse response = RegisterResponse.builder()
        .userName(users.getName())
        .email(users.getEmail())
        .build();
    return response;
  }

  public void verify(String email, String otp) {
    User users = userRepository.findByEmail(email);
    if (users == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
    } else if (users.isVerified()) {
      throw new ResponseStatusException(HttpStatus.ALREADY_REPORTED, "User is already verified");
    } else if (otp.equals(users.getOtp())) {
      users.setVerified(true);
      userRepository.save(users);
    } else {
      throw new RuntimeException("Internal Server error");
    }
  }


  private String generateOTP() {
    Random random = new Random();
    int otpValue = 100000 + random.nextInt(900000);
    return String.valueOf(otpValue);
  }

  private void sendVerificationEmail(String email, String otp) {
    String subject = "Email verification";
    String body = "your verification otp is: " + otp;
    emailService.sendEmail(email, subject, body);
  }

  public String loginUser(RegisterRequest registerRequest) {
    User user = userRepository.findByEmail(registerRequest.getEmail());
    if (user == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Not found");
    }
    if(!user.isVerified()){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"User not verified");
    }
    if(!user.getPassword().equals(registerRequest.getPassword())){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Password is wrong");
    }
    return "Logged in successfully";
  }

}
