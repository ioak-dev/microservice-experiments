package com.hexagonal.user.application.service;

import com.hexagonal.user.application.port.TenantRepository;
import com.hexagonal.user.domain.model.Tenant;
import com.hexagonal.user.domain.service.TenantService;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class TenantServiceImpl implements TenantService {

  private final TenantRepository tenantRepository;

  public TenantServiceImpl(TenantRepository tenantRepository) {
    this.tenantRepository = tenantRepository;
  }

  public Tenant createTenant(Tenant tenant) {
    return tenantRepository.save(tenant);
  }

  public Optional<Tenant> getTenantById(String tenantId) {
    return tenantRepository.findById(tenantId);
  }

  public List<Tenant> getAllTenants() {
    return tenantRepository.findAll();
  }
}
