package com.cabinparser.application.schedulers;

import com.cabinparser.application.Constants;
import com.cabinparser.domain.cabin.Cabin;
import com.cabinparser.domain.cabin.CabinRepository;
import com.cabinparser.infrastructure.api.googlemaps.GeoResponse;
import com.cabinparser.infrastructure.api.googlemaps.GoogleMapsApiClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.context.annotation.Value;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
public class UpdateDistrictRegionAndLocalityOfCabins {

  protected ObjectMapper objectMapper = new ObjectMapper();
  @Inject
  private CabinRepository cabinRepository;
  @Inject
  private GoogleMapsApiClient googleMapsApiClient;
  @Value("${google.api-key}")
  private String key;

  // @Scheduled(initialDelay = "1s", fixedDelay = "1d")
  public void parse() {
    log.info("Starting UpdateDistrictRegionAndLocalityOfCabins job");
    processCabins();
    log.info("Finished UpdateDistrictRegionAndLocalityOfCabins");
  }

  protected void processCabins() {

    // get all related cabins
    final List<Cabin> cabins = cabinRepository.getByVendor(Constants.MEGAUBYTOVANIE);

    cabins.forEach(cabin -> {
      if (cabin.getRegion() == null ||
        cabin.getDistrict() == null
        // not locality as that can be null
      ) {
        log.info("Starting updating geolocation for Cabin {}", cabin.getName());

        final GeoResponse response = googleMapsApiClient.geoRequest(
          cabin.getGpsPositionLatitude() + "," + cabin.getGpsPositionLongitude(),
          key
        );
        final AtomicReference<String> locality = new AtomicReference<>(null);
        final AtomicReference<String> region = new AtomicReference<>(null);
        final AtomicReference<String> district = new AtomicReference<>(null);
        final AtomicReference<String> country = new AtomicReference<>(null);

        response.results().stream().findFirst().ifPresent(result -> {
          result.address_components().forEach(addressComponent -> {
            if (addressComponent.types().contains("administrative_area_level_1")) {
              log.info("Region found {}", addressComponent.long_name());
              region.set(addressComponent.long_name());
            }
            if (addressComponent.types().contains("administrative_area_level_2")) {
              log.info("District found {}", addressComponent.long_name());
              district.set(addressComponent.long_name());
            }
            if (addressComponent.types().contains("locality")) {
              log.info("Locality found {}", addressComponent.long_name());
              locality.set(addressComponent.long_name());
            }
            if (addressComponent.types().contains("country")) {
              log.info("Country found {}", addressComponent.short_name());
              country.set(addressComponent.short_name());
            }
          });
        });
        cabin.setRegion(region.get());
        cabin.setDistrict(district.get());
        cabin.setLocality(locality.get());
        cabin.setCountry(country.get());
        final String geocodingDump;
        try {
          geocodingDump = objectMapper.writeValueAsString(response);
        } catch (final JsonProcessingException e) {
          throw new RuntimeException(e);
        }
        cabin.setGeocodingDump(geocodingDump);

        cabinRepository.update(cabin);
        log.info("Geolocation updated for Cabin {}", cabin.getName());
        try {
          Thread.sleep(Duration.ofMillis(100));
        } catch (final InterruptedException e) {
          throw new RuntimeException(e);
        }
      }
    });

  }

}
