package com.cabinparser.domain.cabinocupancy;

import com.cabinparser.domain.cabin.Cabin;
import jakarta.inject.Singleton;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
@AllArgsConstructor
public class CabinOccupancyService {

  protected CabinOccupancyRepository cabinOccupancyRepository;

  public void store(final CabinOccupancy cabinOccupancy) {
    cabinOccupancyRepository.store(cabinOccupancy);
  }

  public BigDecimal calculateOccupancy(final Cabin cabin, final LocalDate cabinParserStartDate, final LocalDate now) {
    final int occupiedDays = cabinOccupancyRepository.getOccupanciesByCabinIdAndDateRange(
      cabin.getId(), cabinParserStartDate, now.minusDays(1)
    );
    final long totalDays = cabinParserStartDate.until(now, ChronoUnit.DAYS);
    final BigDecimal occupancy = BigDecimal.valueOf(occupiedDays).divide(
      BigDecimal.valueOf(totalDays),
      2,
      RoundingMode.HALF_UP
    );
    log.info("Occupancy for cabin {} is {}", cabin.getId(), occupancy);
    return occupancy;
  }
}

