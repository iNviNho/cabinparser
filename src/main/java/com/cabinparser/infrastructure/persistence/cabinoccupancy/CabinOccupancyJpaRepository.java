package com.cabinparser.infrastructure.persistence.cabinoccupancy;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;
import java.time.LocalDate;
import java.util.List;

@JdbcRepository(
  dialect = Dialect.POSTGRES
)
public interface CabinOccupancyJpaRepository extends CrudRepository<CabinOccupancyJpaEntity, Long> {

  @Query(
    value = "SELECT COUNT(*) FROM cabin_occupancy WHERE cabin_id = :cabinId AND date BETWEEN :startDate AND :endDate")
  int getNumberOfOccupanciesByCabinIdAndDateRange(int cabinId, LocalDate startDate, LocalDate endDate);

  @Query(
    value = "SELECT * FROM cabin_occupancy WHERE cabin_id = :cabinId AND date BETWEEN :startDate AND :endDate")
  List<CabinOccupancyJpaEntity> getCabinOccupanciesByCabinIdAndDateRange(int cabinId, LocalDate startDate,
                                                                         LocalDate endDate);

}
