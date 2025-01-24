package com.example.bff.controller;

import com.example.bff.dto.WebAppDTO;
import com.example.bff.service.WebAppService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/webapp")
public class WebAppController {

  private final WebAppService webAppService;

  public WebAppController(WebAppService webAppService) {
    this.webAppService = webAppService;
  }

  @GetMapping("/data")
  public WebAppDTO getWebAppData(
      @RequestParam Long userId,
      @RequestParam Long productId,
      @RequestParam Long orderId) {
    return webAppService.getAggregatedData(userId, productId, orderId);
  }

}
