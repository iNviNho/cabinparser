package com.cabinparser.application.schedulers.sub;

import com.cabinparser.domain.propertyforsale.PropertyForSale;
import com.cabinparser.domain.propertyforsale.PropertyForSaleRepository;
import jakarta.inject.Singleton;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.function.Function;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

@Singleton
@Slf4j
@AllArgsConstructor
public class UpdatePriceDiffsForPropertiesForSale {

  @NonNull
  protected PropertyForSaleRepository propertyForSaleRepository;

  public void parse() {
    parseCategory("REKREACNY_POZEMOK");
    parseCategory("STAVEBNY_POZEMOK");
    parseCategory("DOM_NA_PREDAJ");
    parseCategory("CHATA_NA_PREDAJ");
    log.info("Finished parsing properties for sale and updating price differences.");
  }

  void parseCategory(final String category) {

    final var properties = propertyForSaleRepository.getAllForSaleByCategory(category);
    if (properties.isEmpty()) {
      log.info("No properties found for category: {}", category);
      return;
    }

    final HashMap<String, Pair<Integer, BigDecimal>> dataset = new HashMap<>();
    // first we calculate the average price per square meter for the region, district, and locality
    properties.forEach(property -> {
      if (property.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
        return;
      }
      if (property.getRegion() != null) {
        parsePart(property, PropertyForSale::getRegion, dataset);
      }
      if (property.getRegion() != null && property.getDistrict() != null) {
        parsePart(property, p -> p.getRegion() + p.getDistrict(), dataset);
      }
      if (property.getRegion() != null && property.getDistrict() != null && property.getLocality() != null) {
        parsePart(property, p -> p.getRegion() + p.getDistrict() + p.getLocality(), dataset);
      }
    });

    // update the properties with the calculated price per square meter
    properties.forEach(propertyForSale -> {
        if (category.equals("STAVEBNY_POZEMOK") || category.equals("REKREACNY_POZEMOK")) {
          if (propertyForSale.getPrice().compareTo(BigDecimal.ZERO) <= 0 || !propertyForSale.hasSaneEstateForLand()) {
            return;
          }
        }

        if (category.equals("CHATA_NA_PREDAJ") || category.equals("DOM_NA_PREDAJ")) {
          if (propertyForSale.getPrice().compareTo(BigDecimal.ZERO) <= 0 || !propertyForSale.hasSaneEstateForHouse()) {
            return;
          }
        }

        final String region = propertyForSale.getRegion();
        final String district = propertyForSale.getDistrict();
        final String locality = propertyForSale.getLocality();
        final BigDecimal pricePerSquareMeter = (category.equals("STAVEBNY_POZEMOK") || category.equals("REKREACNY_POZEMOK"))
            ? propertyForSale.getPricePerSquareMeterForLand()
            : propertyForSale.getPricePerSquareMeterForHouse();

        if (region != null) {
            final var regionKey = region;
            final var regionPair = dataset.get(regionKey);
            if (regionPair != null) {
              final var regionPricePerSquareMeter = regionPair.getRight().divide(BigDecimal.valueOf(regionPair.getLeft()), BigDecimal.ROUND_HALF_UP);
              // I want to calculate % difference either positive or negative
              final var priceDiff = pricePerSquareMeter.divide(regionPricePerSquareMeter, BigDecimal.ROUND_HALF_UP)
                .subtract(BigDecimal.ONE)
                .multiply(BigDecimal.valueOf(100)); // convert to percentage
              propertyForSale.setRegionPriceToAverageDifference(priceDiff);
            }
        }

        if (region != null && district != null) {
            final var regionDistrictKey = region + district;
            final var regionDistrictPair = dataset.get(regionDistrictKey);
            if (regionDistrictPair != null) {
              final var regionDistrictPricePerSquareMeter = regionDistrictPair.getRight().divide(BigDecimal.valueOf(regionDistrictPair.getLeft()), BigDecimal.ROUND_HALF_UP);
              final var priceDiff = pricePerSquareMeter.divide(regionDistrictPricePerSquareMeter, BigDecimal.ROUND_HALF_UP)
                .subtract(BigDecimal.ONE)
                .multiply(BigDecimal.valueOf(100)); // convert to percentage
              propertyForSale.setDistrictPriceToAverageDifference(priceDiff);
            }
        }

        if (region != null && district != null && locality != null) {
            final var regionDistrictLocalityKey = region + district + locality;
            final var regionDistrictLocalityPair = dataset.get(regionDistrictLocalityKey);
            if (regionDistrictLocalityPair != null) {
              final var regionDistrictLocalityPricePerSquareMeter = regionDistrictLocalityPair.getRight().divide(BigDecimal.valueOf(regionDistrictLocalityPair.getLeft()), BigDecimal.ROUND_HALF_UP);
              final var priceDiff = pricePerSquareMeter.divide(regionDistrictLocalityPricePerSquareMeter, BigDecimal.ROUND_HALF_UP)
                .subtract(BigDecimal.ONE)
                .multiply(BigDecimal.valueOf(100)); // convert to percentage
              propertyForSale.setLocalityPriceToAverageDifference(priceDiff);
            }
        }

        log.info("PropertyForSale {} update price diff to {}", propertyForSale.getTitle(),
          propertyForSale.getRegionPriceToAverageDifference() + " " +
          propertyForSale.getDistrictPriceToAverageDifference() + " " +
          propertyForSale.getLocalityPriceToAverageDifference());
        propertyForSaleRepository.update(propertyForSale);
    });
  }

  void parsePart(PropertyForSale property, Function<PropertyForSale, String> keyFunction, HashMap<String, Pair<Integer, BigDecimal>> datasets) {
    final var key = keyFunction.apply(property);

    // Determine the price based on the property category
    final BigDecimal price = (property.getCategory().equals("STAVEBNY_POZEMOK") || property.getCategory().equals("REKREACNY_POZEMOK"))
        ? (property.hasSaneEstateForLand() ? property.getPricePerSquareMeterForLand() : null)
        : (property.getCategory().equals("CHATA_NA_PREDAJ") || property.getCategory().equals("DOM_NA_PREDAJ"))
            ? (property.hasSaneEstateForHouse() ? property.getPricePerSquareMeterForHouse() : null)
            : null;

    // price is not sane
    if (price == null) {
        return;
    }

    // then we process it
    datasets.putIfAbsent(key, new ImmutablePair<>(0, BigDecimal.ZERO));
    datasets.compute(
      key,
      (k, val) -> new ImmutablePair<>(
        // increase processed properties by 1
        val.getLeft() + 1,
        // increase price per square meter by the current property price per square meter
        val.getRight().add(price)
    ));
  }
}
