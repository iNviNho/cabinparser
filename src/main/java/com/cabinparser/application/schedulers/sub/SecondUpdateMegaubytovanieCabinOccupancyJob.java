package com.cabinparser.application.schedulers.sub;

import com.cabinparser.application.Constants;
import com.cabinparser.domain.cabin.Cabin;
import com.cabinparser.domain.cabin.CabinService;
import com.cabinparser.domain.cabinocupancy.CabinOccupancy;
import com.cabinparser.domain.cabinocupancy.CabinOccupancyService;
import com.cabinparser.infrastructure.api.webapimegaubytovanie.CabinOccupancyResponse;
import com.cabinparser.infrastructure.api.webapimegaubytovanie.WebApiMegaubytovanieApiClient;
import jakarta.inject.Singleton;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
@AllArgsConstructor
public class SecondUpdateMegaubytovanieCabinOccupancyJob {

  private CabinService cabinService;

  private CabinOccupancyService cabinOccupancyService;

  private WebApiMegaubytovanieApiClient webApiMegaubytovanieApiClient;

  // @Scheduled(initialDelay = "1s", fixedDelay = "1d")
  public void parse() {
    log.info("Starting UpdateCabinOccupancyJob job");
    updateCabinOccupancy();
    log.info("Finished UpdateCabinOccupancyJob");
  }

  private void updateCabinOccupancy() {
    final List<Cabin> cabins = cabinService.getByVendor(Constants.MEGAUBYTOVANIE);
    cabins.forEach(this::processCabinOccupancy);
  }

  private void processCabinOccupancy(final Cabin cabin) {
    log.info("Starting updating cabin occupancy for cabin {}", cabin.getName());

    if (shouldSkipCabinUpdate(cabin)) {
      log.info("Skipping cabin occupancy update for cabin {}", cabin.getName());
      return;
    }

    updateCabinForNextMonths(cabin);
    cabin.setLastCalendarOccupancyUpdate(Instant.now());
    cabinService.update(cabin);
    log.info("Cabin occupancy check for {} finished", cabin.getName());
    try {
      Thread.sleep(Duration.ofMillis(250));
    } catch (final InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  private boolean shouldSkipCabinUpdate(final Cabin cabin) {
    if (cabin.lastCalendarUpdateIsOlderThanThreeMonths()) {
      log.info("Skipped: Cabin {} last update was more than 3 months ago.", cabin.getName());
      return true;
    }
    if (cabin.getLastCalendarOccupancyUpdate() != null &&
      cabin.getLastCalendarOccupancyUpdate().isAfter(Instant.now().minus(7, ChronoUnit.DAYS))) {
      log.info("Skipped: Cabin {} was updated less than a 7 days ago.", cabin.getName());
      // return true;
    }
    if (!cabin.isSlovakCabin()) {
      log.info("Skipped: Cabin {} is not in Slovakia.", cabin.getName());
      return true;
    }
    return false;
  }

  private void updateCabinForNextMonths(final Cabin cabin) {
    for (int i = 1; i <= 3; i++) {
      final LocalDate fromDate = LocalDate.now().withDayOfMonth(1).plusMonths(i - 1);
      final LocalDate toDate = LocalDate.now().withDayOfMonth(1).plusMonths(i);

      final CabinOccupancyResponse response = webApiMegaubytovanieApiClient.getCabinOccupancy(
        cabin.getVendorUniqueId(), fromDate, toDate
      );
      processOccupancyResponse(cabin, response);
    }
  }

  private void processOccupancyResponse(final Cabin cabin, final CabinOccupancyResponse response) {
    if (cabin.isCabinPartOfComplex()) {
      response.blockedAccommodationUnitsIntervals().forEach(interval -> {
        if (interval.accommodationUnitId() == cabin.getAccommodationUnitId()) {
          process(cabin, interval.blockingFromDate(), interval.blockingToDate());
        }
      });
    } else {
      response.blockedAccommodationIntervals().forEach(interval -> {
        process(cabin, interval.blockingFromDate(), interval.blockingToDate());
      });
    }
  }

  private void process(final Cabin cabin, final LocalDate fromDate, final LocalDate toDate) {
    for (LocalDate date = fromDate; !date.isEqual(toDate); date = date.plusDays(1)) {
      final CabinOccupancy cabinOccupancy = new CabinOccupancy(date, cabin.getId(), Instant.now());
      cabinOccupancyService.store(cabinOccupancy);
    }
    log.info("Updated cabin {} occupancy for interval {} - {}", cabin.getName(), fromDate, toDate);
  }

  // protected void updateCabinOccupancy() {
  //
  //   // get all related cabins
  //   final List<Cabin> cabins = cabinService.getByVendor(Constants.MEGAUBYTOVANIE);
  //
  //   cabins.forEach(cabin -> {
  //     log.info("Starting updating cabin occupancy for cabin {}", cabin.getName());
  //     final Instant threeMonthsAgo = ZonedDateTime.now().minus(3, ChronoUnit.MONTHS).toInstant();
  //
  //     if (cabin.getLastCalendarUpdate().isBefore(threeMonthsAgo)) {
  //       log.info(
  //         "Cabin occupancy check for cabin {} skipped because last calendar update {} was more than 3 months ago",
  //         cabin.getName(), cabin.getLastCalendarUpdate());
  //     } else if (cabin.getLastCalendarOccupancyUpdate() != null &&
  //       cabin.getLastCalendarOccupancyUpdate().isAfter(Instant.now().minus(1, ChronoUnit.DAYS))) {
  //       log.info(
  //         "Cabin occupancy check for cabin {} skipped because there has to be at least one day passed to recheck occupancy",
  //         cabin.getName());
  //     } else {
  //       // get occupancy for next 3 months and chunk by month
  //       for (int i = 1; i <= 3; i++) {
  //         final LocalDate fromDate = LocalDate.now().withDayOfMonth(1).plusMonths(i - 1);
  //         final LocalDate toDate = LocalDate.now().withDayOfMonth(1).plusMonths(i);
  //
  //         // get occupancy for upcoming X months
  //         final CabinOccupancyResponse response = webApiMegaubytovanieApiClient.getCabinOccupancy(
  //           cabin.getVendorUniqueId(), //
  //           fromDate,
  //           toDate
  //         );
  //         try {
  //           Thread.sleep(Duration.ofMillis(500));
  //         } catch (final InterruptedException e) {
  //           throw new RuntimeException(e);
  //         }
  //
  //         if (cabin.isCabinPartOfComplex()) {
  //           // iterate over unit intervals that are valid for this cabin
  //           final AtomicReference<Integer> accommodationUnitId = new AtomicReference<>(null);
  //           try {
  //             accommodationUnitId.set(cabin.getAccommodationUnitId());
  //           } catch (final JsonProcessingException e) {
  //             throw new RuntimeException(e);
  //           }
  //
  //           response.blockedAccommodationUnitsIntervals().forEach(blockedAccommodationUnitInterval -> {
  //             if (blockedAccommodationUnitInterval.accommodationUnitId() == accommodationUnitId.get()) {
  //               process(
  //                 cabin,
  //                 blockedAccommodationUnitInterval.blockingFromDate(),
  //                 blockedAccommodationUnitInterval.blockingToDate()
  //               );
  //             }
  //           });
  //         } else {
  //           // iterate over intervals
  //           response.blockedAccommodationIntervals().forEach(blockedAccommodationInterval -> {
  //             process(
  //               cabin,
  //               blockedAccommodationInterval.blockingFromDate(),
  //               blockedAccommodationInterval.blockingToDate()
  //             );
  //           });
  //         }
  //       }
  //       cabin.setLastCalendarOccupancyUpdate(Instant.now());
  //       cabinService.update(cabin);
  //       log.info("Cabin occupancy check for {} finished", cabin.getName());
  //     }
  //   });
  //
  // }

  // protected void process(
  //   final Cabin cabin,
  //   final LocalDate blockingFromDate,
  //   final LocalDate blockingToDate
  // ) {
  //   // Iterate over each day in the interval
  //   for (
  //     LocalDate date = blockingFromDate;
  //     !date.isAfter(blockingToDate);
  //     date = date.plusDays(1)) {
  //     final CabinOccupancy cabinOccupancy = new CabinOccupancy(
  //       date,
  //       cabin.getId(),
  //       Instant.now()
  //     );
  //     cabinOccupancyService.store(cabinOccupancy);
  //   }
  //   log.info("Updated cabin {} occupancy for interval {} - {}",
  //     cabin.getName(),
  //     blockingFromDate,
  //     blockingToDate);
  // }

}
