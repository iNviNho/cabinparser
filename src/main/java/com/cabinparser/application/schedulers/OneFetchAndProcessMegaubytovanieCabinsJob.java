package com.cabinparser.application.schedulers;

import com.cabinparser.application.Constants;
import com.cabinparser.domain.cabin.Cabin;
import com.cabinparser.domain.cabin.CabinService;
import com.cabinparser.infrastructure.api.webapimegaubytovanie.AccommodationSearchPayload;
import com.cabinparser.infrastructure.api.webapimegaubytovanie.AccommodationSearchResponse;
import com.cabinparser.infrastructure.api.webapimegaubytovanie.WebApiMegaubytovanieApiClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Singleton;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
@RequiredArgsConstructor
public class OneFetchAndProcessMegaubytovanieCabinsJob {
  protected ObjectMapper objectMapper = new ObjectMapper();
  @NonNull
  protected WebApiMegaubytovanieApiClient webApiMegaubytovanieApiClient;
  @NonNull
  protected CabinService cabinService;

  // @Scheduled(initialDelay = "1s", fixedDelay = "1d")
  public void parse() throws InterruptedException {
    log.info("Starting FetchAndProcessCabinsJob job");

    log.info("Starting processing chaty");
    fetchAndProcessCabins(1, "chaty");
    log.info("Starting processing drevenice");
    fetchAndProcessCabins(2, "drevenice");
    log.info("Starting processing domy-na-strome");
    fetchAndProcessCabins(12, "domy-na-strome");

    log.info("Finished FetchAndProcessCabinsJob");
  }

  protected void fetchAndProcessCabins(
    final int accommodationFilterId,
    final String type
  ) throws InterruptedException {
    final int limit = 25;
    int offset = 0;
    AccommodationSearchResponse
      response;
    do {
      log.info("Processing page with offset {}", offset);
      response = webApiMegaubytovanieApiClient.accommodationsSearch(
        createSearchPayload(accommodationFilterId, offset, limit)
      );
      processPage(response, type);

      offset += limit;
      Thread.sleep(Duration.ofMillis(250));
    } while (!response.accommodations().isEmpty());
  }

  protected void processPage(
    final AccommodationSearchResponse response,
    final String type
  ) {
    response
      .accommodations()
      .forEach(accommodation -> {
        try {
          // does accommodation contain multiple cabins or chalets?
          if (accommodation.multipleAccommodatedGroupsAllowed()) {
            accommodation.accommodationUnits().forEach(accommodationUnit -> {
              if (accommodationUnit.unitType().identifier().equals(Constants.CABIN) ||
                accommodationUnit.unitType().identifier().equals(Constants.CHALET)) {
                processAccommodationUnitAsCabin(type, accommodation, accommodationUnit);
              } else {
                log.debug("Skipping processing accommodation unit of cabin {} because it is {},",
                  accommodation.name(),
                  accommodationUnit.unitType().identifier());
              }
            });
          } else {
            final String accommodationSearchDump;
            try {
              accommodationSearchDump = objectMapper.writeValueAsString(accommodation);
            } catch (final JsonProcessingException e) {
              throw new RuntimeException(e);
            }

            final Cabin cabin = new Cabin(
              null,
              accommodation.name(),
              accommodation.urlFragment(),
              String.valueOf(accommodation.id()),
              type,
              accommodation.basicStats().bedroomsCount(),
              accommodation.basicStats().regularBedsSleepingCapacity(),
              accommodation.basicStats().extraBedsSleepingCapacity(),
              accommodation.basicStats().regularBedsSleepingCapacity()
                + accommodation.basicStats().extraBedsSleepingCapacity(),
              accommodation.ratingAndReviews().rating().average(),
              accommodation.ratingAndReviews().activeReviewsCount(),
              calculateFastAveragePricePerNight(accommodation),
              accommodation.accommodationPriceRange().priceType(),
              accommodation.gpsPosition().latitude(),
              accommodation.gpsPosition().longitude(),
              accommodation.description(),
              Constants.MEGAUBYTOVANIE,
              accommodation.images().stream().map(AccommodationSearchResponse.Accommodation.Image::url).toList(),
              Instant.parse(accommodation.lastCalendarUpdateWhen()),
              Instant.parse(accommodation.createdWhen()),
              Instant.now(),
              accommodationSearchDump,
              null,
              null,
              null,
              null,
              null,
              null,
              null,
              null
            );

            cabinService.processCabin(cabin);
          }
        } catch (final RuntimeException e) {
          log.error("Error processing {}", accommodation, e);
        }
      });
  }

  protected void processAccommodationUnitAsCabin(
    final String type,
    final AccommodationSearchResponse.Accommodation accommodation,
    final AccommodationSearchResponse.Accommodation.AccommodationUnit accommodationUnit
  ) {
    final String accommodationSearchDump;
    try {
      accommodationSearchDump = objectMapper.writeValueAsString(accommodation);
    } catch (final JsonProcessingException e) {
      throw new RuntimeException(e);
    }

    final int regularBeds = accommodationUnit.regularBeds().stream()
      .mapToInt(regularBed ->
        regularBed.singleBedSleepingCapacity() * regularBed.sameBedsCount()
      )
      .sum();
    final int extraBeds = accommodationUnit.extraBeds().stream()
      .mapToInt(extraBed ->
        extraBed.singleBedSleepingCapacity() * extraBed.sameBedsCount()
      )
      .sum();

    final Cabin cabin = new Cabin(
      null,
      accommodationUnit.name() + " | " + accommodation.name(),
      accommodation.urlFragment(),
      String.valueOf(accommodation.id()),
      type,
      accommodationUnit.bedroomsCount(),
      regularBeds,
      extraBeds,
      regularBeds + extraBeds,
      accommodation.ratingAndReviews().rating().average(),
      accommodation.ratingAndReviews().activeReviewsCount(),
      calculateFastAveragePricePerNight(accommodation),
      accommodation.accommodationPriceRange().priceType(),
      accommodation.gpsPosition().latitude(),
      accommodation.gpsPosition().longitude(),
      accommodation.description(),
      Constants.MEGAUBYTOVANIE,
      accommodation.images().stream().map(AccommodationSearchResponse.Accommodation.Image::url).toList(),
      Instant.parse(accommodation.lastCalendarUpdateWhen()),
      Instant.parse(accommodation.createdWhen()),
      Instant.now(),
      accommodationSearchDump,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null
    );

    cabinService.processCabin(cabin);
  }

  protected BigDecimal calculateFastAveragePricePerNight(
    final AccommodationSearchResponse.Accommodation accommodation
  ) {
    if (accommodation.accommodationPriceRange() != null) {
      if (accommodation.accommodationPriceRange().priceType().equals("per-stay-and-night")) {
        return BigDecimal.valueOf(accommodation.accommodationPriceRange().priceTo())
          .add(BigDecimal.valueOf(accommodation.accommodationPriceRange().priceFrom()))
          .divide(BigDecimal.TWO, 2);
      } else if (accommodation.accommodationPriceRange().priceType().equals("per-person-and-night")) {
        return BigDecimal.valueOf(accommodation.accommodationPriceRange().priceTo())
          .add(BigDecimal.valueOf(accommodation.accommodationPriceRange().priceFrom()))
          .divide(BigDecimal.TWO, 2)
          .multiply(BigDecimal.valueOf(accommodation.basicStats().regularBedsSleepingCapacity()
            + accommodation.basicStats().extraBedsSleepingCapacity()));
      } else {
        // TBA
        throw new RuntimeException(
          "Unhandled price type " + accommodation.accommodationPriceRange().priceType());
      }
    }
    return new BigDecimal("-1");
  }

  protected AccommodationSearchPayload createSearchPayload(
    final int accommodationFilterId,
    final int offset,
    final int limit
  ) {
    return new AccommodationSearchPayload(
      "SK",
      offset,
      limit,
      "best-rated",
      50,
      true,
      new AccommodationSearchPayload.Capacity(
        0,
        0,
        0,
        null,
        false
      ),
      List.of(accommodationFilterId),
      new AccommodationSearchPayload.RoomsCounts(
        0,
        0,
        0
      ),
      false,
      true,
      50
    );
  }

}
