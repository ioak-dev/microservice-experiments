package com.hexagonal.user.application.port;

import com.hexagonal.user.domain.model.Tenant;
import java.util.List;
import java.util.Optional;

public interface TenantRepository {

  Tenant save(Tenant tenant);

  Optional<Tenant> findById(String id);


  List<Tenant> findAll();
}
