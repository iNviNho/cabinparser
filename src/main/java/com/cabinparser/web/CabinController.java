package com.cabinparser.web;

import com.cabinparser.application.Constants;
import com.cabinparser.domain.cabin.CabinService;
import com.cabinparser.web.mappers.CabinToCabinResponseMapper;
import com.cabinparser.web.responses.CabinResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Singleton;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;

@Singleton
@Controller("/cabins")
@PermitAll
@AllArgsConstructor
public class CabinController {

  CabinService cabinService;

  CabinToCabinResponseMapper cabinToCabinResponseMapper;

  @Get("/")
  List<CabinResponse> getAllCabins(
    final String region,
    final String district,
    final String locality,
    final String rating,
    final String reviews,
    final String averagePricePerNight,
    final String occupancy
  ) {
    return cabinService.getSlovakCabinsByVendor(Constants.MEGAUBYTOVANIE)
      .stream()
      .filter(
        cabin -> Objects.equals(region, "null") || Objects.equals(region, "All") || cabin.getRegion().equals(region))
      .filter(cabin -> Objects.equals(district, "null") || Objects.equals(district, "All") ||
        cabin.getDistrict().equals(district))
      .filter(cabin -> Objects.equals(locality, "null") || Objects.equals(locality, "All") ||
        Optional.ofNullable(cabin.getLocality()).map(locality::equals).orElse(false))
      .filter(cabin -> Objects.equals(rating, "null") || Objects.equals(rating, "All") ||
        BigDecimal.valueOf(cabin.getRating()).compareTo(new BigDecimal(rating)) >= 0)
      .filter(cabin -> Objects.equals(reviews, "null") || Objects.equals(reviews, "All") ||
        BigDecimal.valueOf(cabin.getReviewsCount()).compareTo(new BigDecimal(reviews)) >= 0)
      .filter(cabin -> Objects.equals(averagePricePerNight, "null") || Objects.equals(averagePricePerNight, "All") ||
        (cabin.getAvgPricePerNight() != null &&
          BigDecimal.valueOf(cabin.getAvgPricePerNight().doubleValue())
            .compareTo(new BigDecimal(averagePricePerNight)) >= 0))
      .filter(cabin -> Objects.equals(occupancy, "null") || Objects.equals(occupancy, "All") ||
        (cabin.getOccupancy() != null &&
          BigDecimal.valueOf(cabin.getOccupancy().doubleValue())
            .compareTo(new BigDecimal(occupancy).divide(new BigDecimal(100))) >= 0))
      .map(cabinToCabinResponseMapper::toCabinResponse)
      .collect(Collectors.toList());
  }
}
