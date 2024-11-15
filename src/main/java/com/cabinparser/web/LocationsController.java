package com.cabinparser.web;

import com.cabinparser.application.Constants;
import com.cabinparser.domain.cabin.Cabin;
import com.cabinparser.domain.cabin.CabinService;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import jakarta.annotation.security.PermitAll;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;

@Controller("/locations")
@PermitAll
@AllArgsConstructor
public class LocationsController {

  CabinService cabinService;

  @Get("/regions")
  public List<String> getRegions() {
    return cabinService.getSlovakCabinsByVendor(Constants.MEGAUBYTOVANIE)
      .stream()
      .map(Cabin::getRegion)
      .distinct()
      .collect(Collectors.toList());
  }

  @Get("/districts")
  public List<String> getDistricts(final String region) {
    return cabinService.getSlovakCabinsByVendor(Constants.MEGAUBYTOVANIE)
      .stream()
      .filter(cabin -> cabin.getRegion().equals(region))
      .map(Cabin::getDistrict)
      .distinct()
      .collect(Collectors.toList());
  }

  @Get("/localities")
  public List<String> getLocalities(final String region, final String district) {
    return cabinService.getSlovakCabinsByVendor(Constants.MEGAUBYTOVANIE)
      .stream()
      .filter(cabin -> cabin.getRegion().equals(region))
      .filter(cabin -> cabin.getDistrict().equals(district))
      .map(Cabin::getLocality)
      .distinct()
      .collect(Collectors.toList());
  }


}
