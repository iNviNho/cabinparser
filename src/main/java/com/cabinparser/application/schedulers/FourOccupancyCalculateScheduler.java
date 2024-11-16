package com.cabinparser.application.schedulers;

import com.cabinparser.application.Constants;
import com.cabinparser.domain.cabin.CabinService;
import com.cabinparser.domain.cabinocupancy.CabinOccupancyService;
import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Singleton;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Singleton
@Slf4j
@RequiredArgsConstructor
public class FourOccupancyCalculateScheduler {

  @NonNull
  CabinService cabinService;

  @NonNull
  CabinOccupancyService cabinOccupancyService;

  LocalDate cabinParserStartDate = LocalDate.of(2024, 10, 1);

  @Scheduled(initialDelay = "1s")
  public void calculateOccupancy() {
    log.info("Calculating occupancy");
    cabinService.getSlovakCabinsByVendor(Constants.MEGAUBYTOVANIE).forEach(cabin -> {
      if (!cabin.lastCalendarUpdateIsOlderThanThreeMonths()) {
        final BigDecimal occupancy =
          cabinOccupancyService.calculateOccupancy(cabin, cabinParserStartDate, LocalDate.now());
        cabin.setOccupancy(occupancy);
      } else {
        log.info("setting null");
        cabin.setOccupancy(null);
      }
      cabinService.update(cabin);
    });
    log.info("Occupancy calculation finished");
  }

}
