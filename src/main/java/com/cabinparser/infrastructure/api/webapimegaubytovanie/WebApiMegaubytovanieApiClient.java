package com.cabinparser.infrastructure.api.webapimegaubytovanie;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;
import java.time.LocalDate;

@Client("web-api-megaubytovanie-client")
public interface WebApiMegaubytovanieApiClient {

  @Post(
    value = "/v1/public/accommodations/search",
    produces = MediaType.APPLICATION_JSON,
    consumes = MediaType.APPLICATION_JSON
  )
  AccommodationSearchResponse accommodationsSearch(
    @Body final AccommodationSearchPayload payload
  );

  @Get("/v1/public/accommodations/calendar/{id}" +
    "?" +
    "from_date={fromDate}&" +
    "to_date={toDate}"
  )
  CabinOccupancyResponse getCabinOccupancy(
    String id,
    LocalDate fromDate,
    LocalDate toDate
  );

  // create a new method for calling this GET endpoint https://web-api.megaubytovanie.sk/v1/public/accommodations/price-list/11247?from_date=2024-10-20
  @Get("/v1/public/accommodations/price-list/{id}" +
    "?" +
    "from_date={fromDate}"
  )
  CabinPriceListResponse getCabinPriceList(
    String id,
    LocalDate fromDate
  );

}
