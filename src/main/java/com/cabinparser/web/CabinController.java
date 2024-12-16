package com.cabinparser.web;

import com.cabinparser.application.Constants;
import com.cabinparser.domain.cabin.Cabin;
import com.cabinparser.domain.cabin.CabinAttributes;
import com.cabinparser.domain.cabin.CabinService;
import com.cabinparser.domain.cabinocupancy.CabinOccupancy;
import com.cabinparser.domain.cabinocupancy.CabinOccupancyService;
import com.cabinparser.web.mappers.CabinToCabinResponseMapper;
import com.cabinparser.web.responses.CabinResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Patch;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Singleton;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;

@Singleton
@Controller()
@PermitAll
@AllArgsConstructor
public class CabinController {

  CabinService cabinService;

  CabinOccupancyService cabinOccupancyService;

  CabinToCabinResponseMapper cabinToCabinResponseMapper;

  @Get("/cabins")
  List<CabinResponse> getAllCabins(
    final String region,
    final String district,
    final String locality,
    final String rating,
    final String reviews,
    final String averagePricePerNight,
    final String occupancy,
    final String attributes,
    final boolean star
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
      .filter(cabin -> doStringComparison(attributes,
        cabin.getAttributes().stream().map(CabinAttributes::translation).collect(Collectors.toList())))
      .filter(cabin -> cabin.isStar() == star)
      .map(cabinToCabinResponseMapper::toCabinResponse)
      .collect(Collectors.toList());
  }

  @Get("/cabin-occupancy/{cabinId}")
  public List<CabinOccupancy> getCabinOccupancies(
    final String cabinId
  ) {
    return cabinOccupancyService.getOccupancyByCabin(
      Integer.parseInt(cabinId),
      LocalDate.now().minusMonths(12),
      LocalDate.now().plusMonths(12)
    );
  }

  @Patch("/cabins/{cabinId}")
  public CabinResponse toggleCabinStar(
    final String cabinId,
    @Body("star") final boolean star
  ) {
    final Cabin cabin = cabinService.toggleCabinStar(Integer.parseInt(cabinId), star);
    return cabinToCabinResponseMapper.toCabinResponse(cabin);
  }

  @Get("/cabin-attributes")
  List<CabinAttributes> getCabinDistinctAttributes() {
    final List<CabinAttributes> cabinAttributes = new ArrayList<>();

    cabinService.getSlovakCabinsByVendor(Constants.MEGAUBYTOVANIE)
      .stream()
      .map(Cabin::getAttributes)
      .forEach(listOfAttributes -> {
        listOfAttributes.forEach(attribute -> {
          final Optional<CabinAttributes> cabinAttribute = cabinAttributes.stream()
            .filter(ca -> ca.identifier().equals(attribute.identifier()))
            .findFirst();
          if (cabinAttribute.isEmpty()) {
            cabinAttributes.add(attribute);
          }
        });
      });

    return cabinAttributes.stream().sorted(
      Comparator.comparing(CabinAttributes::translation)
    ).toList();
  }

  protected boolean doStringComparison(final String valueGiven, final String valueToCompareAgainst) {
    return Objects.equals(valueGiven, "null") || Objects.equals(valueGiven, "")
      || Arrays.stream(valueGiven.split(","))
      .anyMatch(item -> Optional.ofNullable(valueToCompareAgainst).map(item::equals).orElse(false));
  }

  protected boolean doStringComparison(final String valueGiven, final List<String> valueToCompareAgainst) {

    return Objects.equals(valueGiven, "null") || Objects.equals(valueGiven, "")
      || Arrays.stream(valueGiven.split(","))
      .allMatch(loc -> valueToCompareAgainst.stream().anyMatch(loc::equals));
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
