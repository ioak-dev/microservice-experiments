package com.example.user.controller;


import com.example.user.model.RegisterRequest;
import com.example.user.model.RegisterResponse;
import com.example.user.service.UserService;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserAuthController {

  @Autowired
  private UserService userService;


  @PostMapping("/register")
  public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest registerRequest){
    RegisterResponse registerResponse = userService.register(registerRequest);
    return new ResponseEntity<>(registerResponse, HttpStatus.CREATED);
  }

  @PostMapping("/verify")
  public ResponseEntity<?> verifyUser(@RequestParam String email,@RequestParam String otp){
    try {
      userService.verify(email, otp);
      Map<String,String> stringMap=new HashMap<>();
      stringMap.put("message","User verified successfully");
      return new ResponseEntity<>(stringMap,HttpStatus.OK);
    }catch (RuntimeException e){
      return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @PostMapping("/login")
  public ResponseEntity<?> loginUser(@RequestBody RegisterRequest registerRequest){
    return new ResponseEntity<>(userService.loginUser(registerRequest),HttpStatus.OK);
  }

}
