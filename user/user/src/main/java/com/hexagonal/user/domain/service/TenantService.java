package com.hexagonal.user.domain.service;

import com.hexagonal.user.application.port.TenantRepository;
import com.hexagonal.user.domain.model.Tenant;
import java.util.List;
import java.util.Optional;

public interface TenantService {

  public Tenant createTenant(Tenant tenant);
  public Optional<Tenant> getTenantById(String tenantId);
  public List<Tenant> getAllTenants();

}
