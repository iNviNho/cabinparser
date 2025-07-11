package com.cabinparser.web;

import com.cabinparser.domain.propertyforsale.PropertyForSaleRepository;
import com.cabinparser.web.mappers.PropertyForSaleToPropertyForSaleResponseMapper;
import com.cabinparser.web.responses.PropertyForSaleResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Singleton;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;

@Singleton
@Controller()
@PermitAll
@AllArgsConstructor
public class PropertyForSaleController {

  PropertyForSaleRepository propertyForSaleRepository;
  PropertyForSaleToPropertyForSaleResponseMapper propertyForSaleToPropertyForSaleResponseMapper;

  @Get("/properties-for-sale")
  List<PropertyForSaleResponse> getAllProperties(
    final String region,
    final String district,
    final String locality,
    final String propertyForSale
  ) {
    return propertyForSaleRepository.getAllForSale()
      .stream()
      .filter(cabin -> doStringComparison(region, cabin.getRegion()))
      .filter(cabin -> doStringComparison(district, cabin.getDistrict()))
      .filter(cabin -> doStringComparison(locality, cabin.getLocality()))
      .filter(cabin -> cabin.getGpsPositionLatitude() != null)
      .filter(cabin -> cabin.getGpsPositionLongitude() != null)
      .filter(cabin -> doStringComparison(propertyForSale, cabin.getCategory()))
      .map(propertyForSaleToPropertyForSaleResponseMapper::toCabinResponse)
      .collect(Collectors.toList());
  }

  protected boolean doStringComparison(final String valueGiven, final String valueToCompareAgainst) {
    return Objects.equals(valueGiven, "null") || Objects.equals(valueGiven, "")
      || Arrays.stream(valueGiven.split(","))
      .anyMatch(item -> Optional.ofNullable(valueToCompareAgainst).map(item::equals).orElse(false));
  }
}
