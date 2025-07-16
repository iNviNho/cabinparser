package com.cabinparser.application.schedulers.sub;

import com.cabinparser.domain.propertyforsale.PropertyForSale;
import com.cabinparser.domain.propertyforsale.PropertyForSaleRepository;
import com.cabinparser.infrastructure.api.topreality.TopRealityApiClient;
import jakarta.inject.Singleton;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@Singleton
@Slf4j
@AllArgsConstructor
public class FetchAndProcessTopRealityCabinsForSale {

  @NonNull
  protected TopRealityApiClient topRealityApiClient;

  @NonNull
  protected PropertyForSaleRepository propertyForSaleRepository;

  public void parse() {
    parseSubType(201, "CHATA_NA_PREDAJ");
    parseSubType(204, "DOM_NA_PREDAJ");
    parseSubType(801, "REKREACNY_POZEMOK");
    parseSubType(802, "STAVEBNY_POZEMOK");
  }

  void parseSubType(final int subType, final String category) {
    String response = topRealityApiClient.getCabinsForSale(1, subType);

    final Document doc = Jsoup.parse(response);
    final Elements cabinsHrefs = doc.select(".estate h2.card-title a");

    int startPage = 1;
    final int endPage = Integer.parseInt(doc.select(".pagination .page-item.last-page a").first().text());

    while (startPage < endPage) {
      log.info("Found {} properties for sale, processing pages from {} to {}", cabinsHrefs.size(), startPage, endPage);
      processPage(startPage, subType, category);
      log.info("Processed page {}/{}", startPage, endPage);
      startPage++;
    }
    log.info("Finished processing top reality properties for sale");
  }

  void processPage(final int page, final int subType, final String category) {
    String response = topRealityApiClient.getCabinsForSale(page, subType);

    final Document doc = Jsoup.parse(response);
    final List<Element> cabinsHrefs = doc.select(".estate h2.card-title a").stream().toList();

    cabinsHrefs.forEach(cabinHref -> {
        final String cabinUrl = cabinHref.attr("href");
        log.info("Processing property URL: {}", cabinUrl);

        try {
            final Document cabinDoc = Jsoup.connect(cabinUrl).get();
            final var title = cabinDoc.select(".container .contentDetail h1").text();
            var price = cabinDoc.select(".container .contentDetail .gallery-info > .row .properties .priceContainer .price")
              .text();
            final var locality = getAttribute(cabinDoc, "Lokalita");
            final var updatedAt = getAttribute(cabinDoc, "Aktualiz");
            final var houseEstate = getAttribute(cabinDoc, "Zastavaná plocha");
            final var floorEstate = getAttribute(cabinDoc, "Úžitková plocha");
            final var estate = getAttribute(cabinDoc, "pozemok");
            final var description = cabinDoc.select(".description > p").html();
            final String mapCoordsGpsX = cabinDoc.select("#map_canvas").attr("data-gpsx");
            final String mapCoordsGpsY = cabinDoc.select("#map_canvas").attr("data-gpsy");

            final var pictures = cabinDoc.select(".container .contentDetail .gallery-info .gallery img")
                .stream()
                .map(img -> "https://topreality.sk" + img.attr("data-src"))
                .map(item -> item.replaceAll("-(\\d+)\\.jpg", "-18.jpg"))
                .toList();

            final var cabinForSale = new PropertyForSale(
                UUID.randomUUID(),
                title,
                description,
                extractDigits(price, false),
                locality,
                null,
                null,
                null,
                Instant.now(), // Assuming createdAt is now, adjust if needed
                parseToInstant(updatedAt),
                extractDigits(houseEstate, true),
                extractDigits(floorEstate, true),
                extractDigits(estate, true),
                !mapCoordsGpsX.isEmpty() ? Double.parseDouble(mapCoordsGpsX) : null,
                !mapCoordsGpsY.isEmpty() ? Double.parseDouble(mapCoordsGpsY) : null,
                pictures,
                cabinUrl,
                null,
                false,
                category,
                null,
                null,
                null
            );

            // price is for m2
            if (price.contains("m2") && (
                category.equals("REKREACNY_POZEMOK") ||
                category.equals("STAVEBNY_POZEMOK")
            )) {
              // remove EUR/m2
              price = price.replace("EUR/m2", "").trim();
              // remove €/m2
              price = price.replace("€/m2", "").trim();
              // replace , for .
              price = price.replace(",", ".");

              // and multiply with estate sq m
              cabinForSale.setPrice((new BigDecimal(price)).multiply(cabinForSale.getEstate()));
            }

            final var cabinForSaleExisting = propertyForSaleRepository.getByLink(cabinForSale.getLink());
            if (cabinForSaleExisting.isPresent()) {

              final var cabinForSaleToUpdate = cabinForSaleExisting.get();
              log.info("Property already exists in the repository, updating: {}", cabinForSaleToUpdate.getLink());

              cabinForSaleToUpdate.setTitle(title);
              cabinForSaleToUpdate.setDescription(description);
              cabinForSaleToUpdate.setPrice(cabinForSale.getPrice());
              cabinForSaleToUpdate.setUpdatedAt(Instant.now());
              cabinForSaleToUpdate.setCategory(category);

              propertyForSaleRepository.update(cabinForSaleToUpdate);

              log.info("Property updated: {} from page: {}", title, page);
              return; // Skip if the cabin already exists
            }
            propertyForSaleRepository.store(cabinForSale);

            log.info("Property processed: {} from page: {}", title, page);
        } catch (Exception e) {
            log.error("Error processing Property URL {}", cabinUrl, e);
        }
    });
  }

  public String getAttribute(final Document cabinDoc, final String attributeName) {
    return cabinDoc.select(".container .contentDetail .gallery-info > .row .properties li")
      .stream()
      .filter(item -> item.text().contains(attributeName))
      .findFirst()
      .map(item -> item.select("strong"))
      .stream()
      .findFirst()
      .map(Elements::text)
      .orElse("N/A");
  }

  BigDecimal extractDigits(@NonNull final String input, final boolean removeLastChar) {
    final String sanitizedInput = input.replaceAll("[^\\d]", ""); // Remove non-digit characters
    if (!sanitizedInput.isEmpty()) {
      // removing last char because text contains value like 52 m2
      if (removeLastChar) {
        return new BigDecimal(removeLastChar(sanitizedInput));
      } else {
        return new BigDecimal(sanitizedInput);
      }
    }
    return BigDecimal.ZERO;
  }

  Instant parseToInstant(final String updatedAt) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.M.yyyy HH:mm:ss");
    LocalDateTime localDateTime = LocalDateTime.parse(updatedAt, formatter);
    return localDateTime.atZone(ZoneId.of("Europe/Vienna")).toInstant();
  }

  String removeLastChar(final String input) {
    if (input != null && input.length() > 0) {
        return input.substring(0, input.length() - 1);
    }
    return input; // Return the original string if it's null or empty
  }

}
