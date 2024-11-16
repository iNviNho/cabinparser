package com.cabinparser.application.schedulers;

import com.cabinparser.application.Constants;
import com.cabinparser.domain.cabin.Cabin;
import com.cabinparser.domain.cabin.CabinRepository;
import com.cabinparser.domain.cabin.CabinService;
import com.cabinparser.infrastructure.api.webapimegaubytovanie.CabinPriceListResponse;
import com.cabinparser.infrastructure.api.webapimegaubytovanie.WebApiMegaubytovanieApiClient;
import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Singleton;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Singleton
@Slf4j
@AllArgsConstructor
public class FifthCalculateAveragePricePerNightScheduler {

  CabinRepository cabinRepository;

  CabinService cabinService;

  WebApiMegaubytovanieApiClient webApiMegaubytovanieApiClient;

  @Scheduled(initialDelay = "1s", fixedDelay = "1d")
  void handle() {
    final List<Cabin> cabins = cabinRepository.getByVendor(Constants.MEGAUBYTOVANIE);
    cabins.forEach(cabin -> {
      if (!cabin.isSlovakCabin()) {
        return;
      }
      if (cabin.getAvgPricePerNight() != null) {
        return;
      }
      final CabinPriceListResponse response = webApiMegaubytovanieApiClient.getCabinPriceList(
        cabin.getVendorUniqueId(),
        LocalDate.now()
      );

      cabinService.processCabinPriceList(response, cabin);
      try {
        Thread.sleep(Duration.ofMillis(250));
      } catch (final InterruptedException e) {
        throw new RuntimeException(e);
      }
    });
    log.info("Average price per night calculated for {} cabins", cabins.size());
  }
}
