package com.example.bff.controller;

import com.example.bff.dto.MobileAppDTO;
import com.example.bff.service.MobileAppService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mobileapp")
public class MobileAppController {

  private final MobileAppService mobileAppService;

  public MobileAppController(MobileAppService mobileAppService) {
    this.mobileAppService = mobileAppService;
  }

  @GetMapping("/data")
  public MobileAppDTO getMobileAppData(@RequestParam Long userId) {
    return mobileAppService.getMobileAppData(userId);
  }

}
