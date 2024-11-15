package com.cabinparser.infrastructure.persistence.cabinoccupancy;

import com.cabinparser.application.Utils;
import com.cabinparser.domain.cabinocupancy.CabinOccupancy;
import com.cabinparser.domain.cabinocupancy.CabinOccupancyRepository;
import io.micronaut.data.exceptions.DataAccessException;
import jakarta.inject.Singleton;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Singleton
@Slf4j
@AllArgsConstructor
public class CabinOccupancyRepositoryJpaImpl implements CabinOccupancyRepository {

  private final CabinOccupancyJpaRepository cabinOccupancyJpaRepository;
  private final CabinOccupancyJpaEntityMapper cabinOccupancyJpaEntityMapper;

  @Override
  public void store(final CabinOccupancy cabinOccupancy) {
    try {
      this.cabinOccupancyJpaRepository.save(
        cabinOccupancyJpaEntityMapper.toJpaEntity(cabinOccupancy)
      );
    } catch (final DataAccessException e) {
      // Ignore the exception if it's due to a duplicate key
      if (Utils.isDuplicateKeyException(e)) {
        log.info("Duplicate entry {}", cabinOccupancy);
      } else {
        // Rethrow the exception if it's not related to a duplicate key
        throw e;
      }
    }
  }

  @Override
  public int getOccupanciesByCabinIdAndDateRange(
    final int cabinId,
    final LocalDate startDate,
    final LocalDate endDate
  ) {
    return this.cabinOccupancyJpaRepository.getNumberOfOccupanciesByCabinIdAndDateRange(
      cabinId, startDate, endDate);
  }

}
