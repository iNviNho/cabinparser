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
import java.util.Arrays;
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
      .filter(cabin -> doStringComparison(region, cabin.getRegion()))
      .filter(cabin -> doStringComparison(district, cabin.getDistrict()))
      .filter(cabin -> doStringComparison(locality, cabin.getLocality()))
      .filter(cabin -> doNumericComparison(rating, BigDecimal.valueOf(cabin.getRating())))
      .filter(cabin -> doNumericComparison(reviews, BigDecimal.valueOf(cabin.getReviewsCount())))
      .filter(cabin -> doNumericComparison(averagePricePerNight, cabin.getAvgPricePerNight()))
      .filter(cabin -> cabin.getOccupancy() != null &&
        doNumericComparison(occupancy, cabin.getOccupancy().multiply(BigDecimal.valueOf(100))))
      .map(cabinToCabinResponseMapper::toCabinResponse)
      .collect(Collectors.toList());
  }

  protected boolean doStringComparison(final String valueGiven, final String valueToCompareAgainst) {
    return Objects.equals(valueGiven, "null") || Objects.equals(valueGiven, "")
      || Arrays.stream(valueGiven.split(","))
      .anyMatch(loc -> Optional.ofNullable(valueToCompareAgainst).map(loc::equals).orElse(false));
  }

  protected boolean doNumericComparison(final String valueGiven, final BigDecimal valueToCompareAgainst) {
    if (Objects.equals(valueGiven, "null")) {
      return true;
    }
    if (valueToCompareAgainst == null) {
      return false;
    }
    final String[] arraySplit = valueGiven.split(",");
    final BigDecimal min = new BigDecimal(arraySplit[0].replace(",", "."));
    final BigDecimal max = new BigDecimal(arraySplit[1].replace(",", "."));
    return valueToCompareAgainst.compareTo(min) >= 0 && valueToCompareAgainst.compareTo(max) <= 0;
  }
}
