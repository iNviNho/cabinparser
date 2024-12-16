package com.cabinparser.infrastructure.persistence.cabin;

import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;
import java.util.List;
import java.util.Optional;

@JdbcRepository(
  dialect = Dialect.POSTGRES
)
public interface CabinJpaRepository extends CrudRepository<CabinJpaEntity, Long> {

  List<CabinJpaEntity> findByVendor(String vendorName);

  Optional<CabinJpaEntity> findByVendorAndVendorUniqueIdAndName(final String vendor, final String vendorUniqueId,
                                                                final String name);
  CabinJpaEntity findById(int cabinId);
}
