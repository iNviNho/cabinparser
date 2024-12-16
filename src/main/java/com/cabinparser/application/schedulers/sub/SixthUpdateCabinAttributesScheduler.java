package com.cabinparser.application.schedulers.sub;

import com.cabinparser.application.Constants;
import com.cabinparser.domain.cabin.Cabin;
import com.cabinparser.domain.cabin.CabinRepository;
import com.cabinparser.domain.cabin.CabinService;
import com.cabinparser.infrastructure.api.webapimegaubytovanie.AccommodationDetailResponse;
import com.cabinparser.infrastructure.api.webapimegaubytovanie.WebApiMegaubytovanieApiClient;
import io.micronaut.context.annotation.Value;
import jakarta.inject.Singleton;
import java.time.Duration;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Singleton
@Slf4j
@AllArgsConstructor
public class SixthUpdateCabinAttributesScheduler {

  CabinRepository cabinRepository;

  CabinService cabinService;

  WebApiMegaubytovanieApiClient webApiMegaubytovanieApiClient;

  @Value("${webapi.token}")
  String webApiToken;

  // @Scheduled(initialDelay = "1s", fixedDelay = "1d")
  public void process() {
    final List<Cabin> cabins = cabinRepository.getByVendor(Constants.MEGAUBYTOVANIE);
    cabins.forEach(cabin -> {
      if (!cabin.isSlovakCabin()) {
        return;
      }

      final AccommodationDetailResponse response = webApiMegaubytovanieApiClient.getAccommodationDetail(
        "Bearer " + webApiToken,
        cabin.getVendorUniqueId()
      );

      cabinService.processAccommodationDetailResponse(response, cabin);
      try {
        Thread.sleep(Duration.ofMillis(250));
      } catch (final InterruptedException e) {
        throw new RuntimeException(e);
      }
    });
    log.info("Updated cabin attributes for {} cabins", cabins.size());
  }
}
