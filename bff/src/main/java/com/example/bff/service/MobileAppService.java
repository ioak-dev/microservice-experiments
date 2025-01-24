package com.example.bff.service;

import com.example.bff.client.UserClient;
import com.example.bff.dto.MobileAppDTO;
import com.example.bff.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MobileAppService {

  @Autowired
  private UserClient userClient;

  public MobileAppDTO getMobileAppData(Long userId) {
    log.debug("Get data for mobile app");
    UserDTO user = userClient.getUserById(userId);
    return new MobileAppDTO(user.getUsername(), user.getEmail());
  }

}
