package com.cabinparser.infrastructure.persistence.keyvalue;

import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

@JdbcRepository(
  dialect = Dialect.POSTGRES
)
public interface KeyValueJpaRepository extends CrudRepository<KeyValueJpaEntity, String> {

}
