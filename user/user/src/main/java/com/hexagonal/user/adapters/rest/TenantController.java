package com.hexagonal.user.adapters.rest;

import com.hexagonal.user.domain.model.Tenant;
import com.hexagonal.user.domain.model.User;
import com.hexagonal.user.domain.service.TenantService;
import java.util.List;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tenant")
public class TenantController {

  private final TenantService tenantService;

  public TenantController(@Lazy TenantService tenantService) {
    this.tenantService = tenantService;
  }

  @PostMapping
  public ResponseEntity<Tenant> createTenant(@RequestBody Tenant tenant) {
    Tenant createdTenant = tenantService.createTenant(tenant);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdTenant);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Tenant> getTenant(@PathVariable String id) {
    return ResponseEntity.ok(tenantService.getTenantById(id).orElseThrow());
  }


  @GetMapping()
  public ResponseEntity<List<Tenant>> getAllTenant() {
    return ResponseEntity.ok(tenantService.getAllTenants());
  }
}
