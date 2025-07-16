package com.cabinparser.application.schedulers.sub;

import com.cabinparser.domain.propertyforsale.PropertyForSaleRepository;
import jakarta.inject.Singleton;
import java.io.IOException;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;

@Singleton
@Slf4j
@AllArgsConstructor
public class UpdateAvailabilityOfPropertiesForSale {

  @NonNull
  protected PropertyForSaleRepository propertyForSaleRepository;

  public void parse() {
    parseCategory("REKREACNY_POZEMOK");
    parseCategory("STAVEBNY_POZEMOK");
    parseCategory("DOM_NA_PREDAJ");
    parseCategory("CHATA_NA_PREDAJ");
    log.info("Finished updating availability of properties for sale.");
  }

  void parseCategory(final String category) {

    final var properties = propertyForSaleRepository.getAllForSaleByCategory(category);
    if (properties.isEmpty()) {
      log.info("No properties found for category: {}", category);
      return;
    }

    // first we calculate the average price per square meter for the region, district, and locality
    properties.forEach(property -> {
      // check what link returns
      // call property.link and see if it returns 404
      try {
        Jsoup.connect(property.getLink()).get();
        log.info("Property with ID {} and link {} is still available.",
            property.getId(), property.getLink());
      } catch (IOException e) {
        if (e instanceof HttpStatusException && ((HttpStatusException) e).getStatusCode() == 404) {
          property.setDeletedAt(Instant.now());
          propertyForSaleRepository.update(property);
          log.info("Property with ID {} and link {} is no longer available, marking as deleted.",
              property.getId(), property.getLink());
        } else {
          throw new RuntimeException(e);
        }
      }
      // sleep for 100ms
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        log.error("Thread interrupted while sleeping", e);
      }
    });
  }
}
