package com.cabinparser.infrastructure.persistence.cabinforsale;

import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@JdbcRepository(
  dialect = Dialect.POSTGRES
)
public interface PropertyForSaleJpaRepository extends CrudRepository<PropertyForSale, UUID> {

  Optional<PropertyForSale> getByLink(String link);

  List<PropertyForSale> findByCategory(String category);
}
