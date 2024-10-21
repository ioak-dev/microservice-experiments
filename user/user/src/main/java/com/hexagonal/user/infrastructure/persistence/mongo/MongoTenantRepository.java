package com.hexagonal.user.infrastructure.persistence.mongo;


import com.hexagonal.user.application.port.TenantRepository;
import com.hexagonal.user.domain.model.Tenant;
import java.util.List;
import java.util.Optional;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

@Repository
@Profile("mongo")
public class MongoTenantRepository implements TenantRepository {

  private final MongoTemplate mongoTemplate;

  public MongoTenantRepository(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  @Override
  public Tenant save(Tenant tenant) {
    return mongoTemplate.save(tenant);
  }

  @Override
  public Optional<Tenant> findById(String id) {
    return Optional.ofNullable(mongoTemplate.findById(id, Tenant.class));
  }

  @Override
  public List<Tenant> findAll() {
    return mongoTemplate.findAll(Tenant.class);
  }
}


