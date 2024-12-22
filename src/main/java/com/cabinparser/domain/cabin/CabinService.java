package com.cabinparser.domain.cabin;

import com.cabinparser.infrastructure.api.webapimegaubytovanie.AccommodationDetailResponse;
import com.cabinparser.infrastructure.api.webapimegaubytovanie.CabinPriceListResponse;
import io.micronaut.cache.annotation.CacheConfig;
import io.micronaut.cache.annotation.CacheInvalidate;
import io.micronaut.cache.annotation.Cacheable;
import jakarta.inject.Singleton;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Singleton
@Slf4j
@AllArgsConstructor
@CacheConfig(value = "cabins")
public class CabinService {

  private final CabinRepository cabinRepository;

  public void processCabin(final Cabin cabin) {
    final Optional<Cabin> existingCabin = cabinRepository.getByVendorAndVendorUniqueIdAndName(cabin.getVendor(),
      cabin.getVendorUniqueId(), cabin.getName());
    if (existingCabin.isEmpty()) {
      cabinRepository.store(cabin);
      log.info("New Cabin stored {}", cabin.getName());
    } else {
      log.info("Cabin {} already exists", cabin.getName());
      // let's update only specific fields
      final Cabin existing = existingCabin.get();
      existing.setReviewsCount(cabin.getReviewsCount());
      existing.setRating(cabin.getRating());
      existing.setDescription(cabin.getDescription());
      existing.setImages(cabin.getImages());
      existing.setLastCalendarUpdate(cabin.getLastCalendarUpdate());
      existing.setSearchDump(cabin.getSearchDump());
      cabinRepository.update(existing);
      log.info("Cabin {} updated", cabin.getName());
    }
  }

  public List<Cabin> getByVendor(final String vendor) {
    return cabinRepository.getByVendor(vendor);
  }

  @Cacheable
  public List<Cabin> getSlovakCabinsByVendor(final String vendor) {
    return cabinRepository.getByVendor(vendor)
      .stream().filter(Cabin::isSlovakCabin)
      .collect(Collectors.toList());
  }

  @CacheInvalidate(all = true)
  public Cabin toggleCabinStar(int cabinId, boolean star) {
    final Cabin cabin = cabinRepository.getById(cabinId);
    cabin.setStar(star);
    cabinRepository.update(cabin);
    return cabin;
  }

  public void update(final Cabin cabin) {
    cabinRepository.update(cabin);
  }

  public void processCabinPriceList(final CabinPriceListResponse response, final Cabin cabin) {
    final List<CabinPriceListResponse.Season> seasons = response.priceList().seasons();
    if (seasons.isEmpty()) {
      log.warn("No seasons found for cabin {}", cabin.getName());
      return;
    }

    processPerPersonAndNight(cabin, seasons);
  }

  private void processPerPersonAndNight(
    final Cabin cabin,
    final List<CabinPriceListResponse.Season> seasons
  ) {
    // iterate through seasons and calculate weighed average price per night
    // each season has a price list with price list items, you just need to get first item
    // and get average of minPrice and maxPrice
    BigDecimal totalCabinPricePerYear = BigDecimal.ZERO;
    int totalDaysPerYear = 0;
    for (final CabinPriceListResponse.Season season : seasons) {
      if (season.bookingStrategy().equals("booking-disabled")) {
        continue;
      }
      final BigDecimal cabinPricePerPeriod;
      final long daysInPeriod;
      if (cabin.isCabinPartOfComplex()) {
        if (season.unitsSeasons() == null) {
          // I would skip this as it may only be edge case for one cabin
          log.error("No unit seasons found in season {} for cabin {}", season.name(), cabin.getName());
          continue;
        }
        if (season.unitsSeasons().isEmpty()) {
          log.warn("No unit seasons found in season {} for cabin {}", season.name(), cabin.getName());
          continue;
        }
        final int accommodationUnitId = cabin.getAccommodationUnitId();

        // are we going through unit seasons or
        // iterate through unitsSeasons and find the one where accommodationUnitId matches
        final Optional<CabinPriceListResponse.UnitSeason> unitsSeason = season.unitsSeasons().stream()
          .filter(unitsSeason1 -> unitsSeason1.accommodationUnitId() == accommodationUnitId)
          .findFirst();
        // if not found, skip this season
        if (unitsSeason.isEmpty()) {
          log.warn("No unit season found for cabin {} in season {}", cabin.getName(), season.name());
          continue;
        }

        final Optional<String> priceListItemType = unitsSeason.get().priceListItems().stream()
          .filter(priceListItem1 -> priceListItem1.minPrice() != null && priceListItem1.maxPrice() != null)
          .findFirst()
          .map(CabinPriceListResponse.PriceListItem::itemType);
        if (priceListItemType.isEmpty()) {
          log.warn("No price list item found for season {} in cabin {}", season.name(), cabin.getName());
          continue;
        }

        BigDecimal avgPrice = BigDecimal.valueOf(0);
        BigDecimal items = BigDecimal.valueOf(0);
        for (final CabinPriceListResponse.PriceListItem priceListItem1 : unitsSeason.get().priceListItems()) {
          if (priceListItem1.minPrice() != null && priceListItem1.maxPrice() != null) {
            avgPrice = avgPrice.add(BigDecimal.valueOf((priceListItem1.minPrice() + priceListItem1.maxPrice()) / 2));
            items = items.add(BigDecimal.valueOf(1));
          }
        }

        if (priceListItemType.get().equals("person-per-night")) {
          cabinPricePerPeriod = avgPrice.divide(items, 2, RoundingMode.HALF_UP)
            .multiply(cabin.getTotalSleepingCapacity());
          // stay-per-night
        } else {
          cabinPricePerPeriod = avgPrice.divide(items, 2, RoundingMode.HALF_UP);
        }
        // cabinPricePerPeriod = BigDecimal.valueOf((priceListItem.get().minPrice() + priceListItem.get().maxPrice()) / 2);
        daysInPeriod = ChronoUnit.DAYS.between(season.fromDate(), season.toDate());
      } else {
        final List<CabinPriceListResponse.PriceListItem> priceListItems = season.priceListItems();
        if (priceListItems.isEmpty()) {
          log.warn("No price list items found for season {} in cabin {}", season.name(), cabin.getName());
          continue;
        }

        final Optional<String> priceListItemType = priceListItems.stream()
          .filter(priceListItem1 -> priceListItem1.minPrice() != null && priceListItem1.maxPrice() != null)
          .findFirst()
          .map(CabinPriceListResponse.PriceListItem::itemType);
        if (priceListItemType.isEmpty()) {
          log.warn("No price list item found for season {} in cabin {}", season.name(), cabin.getName());
          continue;
        }

        BigDecimal avgPrice = BigDecimal.valueOf(0);
        BigDecimal items = BigDecimal.valueOf(0);
        for (final CabinPriceListResponse.PriceListItem priceListItem1 : priceListItems) {
          if (priceListItem1.minPrice() != null && priceListItem1.maxPrice() != null) {
            avgPrice = avgPrice.add(BigDecimal.valueOf((priceListItem1.minPrice() + priceListItem1.maxPrice()) / 2));
            items = items.add(BigDecimal.valueOf(1));
          }
        }

        if (priceListItemType.get().equals("person-per-night")) {
          cabinPricePerPeriod = avgPrice.divide(items, 2, RoundingMode.HALF_UP)
            .multiply(cabin.getTotalSleepingCapacity());
          // stay-per-night
        } else {
          cabinPricePerPeriod = avgPrice.divide(items, 2, RoundingMode.HALF_UP);
        }
        daysInPeriod = ChronoUnit.DAYS.between(season.fromDate(), season.toDate());
      }
      totalCabinPricePerYear =
        totalCabinPricePerYear.add(cabinPricePerPeriod.multiply(BigDecimal.valueOf(daysInPeriod)));
      totalDaysPerYear += daysInPeriod;
    }

    if (totalDaysPerYear == 0) {
      log.warn("No days for rent found for cabin {}", cabin.getName());
      return;
    }
    final BigDecimal averagePricePerNight =
      totalCabinPricePerYear.divide(BigDecimal.valueOf(totalDaysPerYear), 2, BigDecimal.ROUND_HALF_UP);
    cabin.setAvgPricePerNight(averagePricePerNight);
    cabinRepository.update(cabin);
    log.info("Updated average price per night for cabin {} to {}",
      cabin.getName(),
      averagePricePerNight);
  }

  public void processAccommodationDetailResponse(final AccommodationDetailResponse response, final Cabin cabin) {
    final List<CabinAttributes> att = new ArrayList<>();
    processItems(response.accommodation_detail().properties_and_equipment().equipment().equipments_for_children(), att);
    processItems(response.accommodation_detail().properties_and_equipment().equipment().exterior_equipments(), att);
    processItems(response.accommodation_detail().properties_and_equipment().equipment().additional_services(), att);
    processItems(response.accommodation_detail().properties_and_equipment().equipment().social_spaces(), att);
    processItems(response.accommodation_detail().properties_and_equipment().equipment().relax_opportunities(), att);
    processItems(response.accommodation_detail().properties_and_equipment().equipment().sport_opportunities(), att);
    processItems(response.accommodation_detail().properties_and_equipment().equipment().agro_tourism_options(), att);
    // convert att to json and store to setAttributes
    cabin.setAttributes(att);
    cabinRepository.update(cabin);

    log.info("Updated attributes for cabin {}", cabin.getName());
  }

  protected void processItems(final List<AccommodationDetailResponse.PropertiesAndEquipment.Equipment.Item> items,
                              final List<CabinAttributes> att) {
    items.forEach(item -> {
      if (item.active()) {
        att.add(
          new CabinAttributes(
            item.identifier(),
            item.name()
          )
        );
      }
    });
  }

}
