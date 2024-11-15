package com.cabinparser.infrastructure.api.webapimegaubytovanie;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CabinOccupancyResponse(
  Instant lastModifiedWhen,
  String checksum,
  List<BlockedAccommodationInterval> blockedAccommodationIntervals,
  List<BlockedAccommodationUnitInterval> blockedAccommodationUnitsIntervals,
  Instant lastCalendarUpdateWhen
) {

  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public record BlockedAccommodationInterval(
    LocalDate blockingFromDate,
    LocalDate blockingToDate,
    int freeSleepingCapacity,
    int freeBedrooms,
    int freeUnits
  ) {
  }

  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public record BlockedAccommodationUnitInterval(
    LocalDate blockingFromDate,
    LocalDate blockingToDate,
    int freeSleepingCapacity,
    int freeBedrooms,
    int freeUnits,
    int accommodationUnitId
  ) {
  }
}
