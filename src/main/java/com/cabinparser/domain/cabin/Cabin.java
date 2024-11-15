package com.cabinparser.domain.cabin;

import com.cabinparser.infrastructure.api.webapimegaubytovanie.AccommodationSearchResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Cabin {

  private Integer id;

  // Basic Cabin Attributes
  private String name;
  private String urlFragment;
  private String vendorUniqueId; // we want this to be string as each vendor can have different namings
  private String type;          // Type of the cabin (e.g., luxury, standard, etc.)
  private int bedroomsCount;        // Number of rooms in the cabin
  private int regularSleepingBeds;        // number of regular beds
  private int extraSleepingBeds;        // number of extra sleeping beds (e.g. couch)
  private int maxPerson;        // Maximum number of persons allowed in the cabin
  private double rating;        // Cabin rating (e.g., 4.5 stars)
  private double reviewsCount;        // Cabin rating (e.g., 4.5 stars)
  // private String condition;     // TBA (bad, good, great)
  private BigDecimal avgPricePerNight;  // Average price per night for renting the cabin
  private String priceType;  // Price type

  // Location Details
  // private String region;        // TBA Region where the cabin is located
  // private String county;        // TBA County where the cabin is located
  private double gpsPositionLatitude; // GPS coordinates of the cabin
  private double gpsPositionLongitude; // GPS coordinates of the cabin

  // Description and Media
  private String description;   // Textual description of the cabin
  private String vendor;        // Source from where the cabin information was fetched (e.g., API, website)
  private List<String> images;  // List of URLs for the cabin's images
  private Instant lastCalendarUpdate;
  private Instant createdWhen;
  private Instant createdAt;

  private String searchDump;
  private String region;
  private String district;
  private String country;

  private String geocodingDump;
  private String locality;

  private Instant lastCalendarOccupancyUpdate;

  private BigDecimal occupancy;

  public boolean isCabinPartOfComplex() {
    return name.contains("|");
  }

  public boolean isSlovakCabin() {
    return country.equals("SK");
  }

  public Integer getAccommodationUnitId() {
    final AccommodationSearchResponse.Accommodation accommodation;
    try {
      accommodation = new ObjectMapper().readValue(
        searchDump, AccommodationSearchResponse.Accommodation.class);
    } catch (final JsonProcessingException e) {
      throw new RuntimeException(e);
    }

    return accommodation.accommodationUnits().stream()
      .filter(accommodationUnit -> name.contains(accommodationUnit.name()))
      .findFirst()
      .map(AccommodationSearchResponse.Accommodation.AccommodationUnit::id)
      .orElseThrow();
  }

  public BigDecimal getTotalSleepingCapacity() {
    return BigDecimal.valueOf(regularSleepingBeds + extraSleepingBeds);
  }

  public boolean lastCalendarUpdateIsOlderThanThreeMonths() {
    return lastCalendarUpdate.isBefore(Instant.now().minusSeconds(60 * 60 * 24 * 30 * 3));
  }

}
