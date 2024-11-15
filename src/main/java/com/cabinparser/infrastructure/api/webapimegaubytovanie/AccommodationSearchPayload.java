package com.cabinparser.infrastructure.api.webapimegaubytovanie;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor
@Getter
public class AccommodationSearchPayload {

  private String preferredCountryCode;
  private int offset;
  private int limit;
  private String sortBy;
  private int imagesCount;
  private boolean withPromotionText;
  private Capacity capacity;
  private List<Integer> accommodationTypeGroupIds;
  private RoomsCounts roomsCounts;
  private boolean appendAccommodationsNearLocation;
  private boolean withUnitsAndRooms;
  private int maxUnitsCount;

  // Getters and Setters...

  @AllArgsConstructor
  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  @Getter
  public static class Capacity {
    private int adultsCount;
    private int childrenCount;
    private int bedroomsCount;
    private String petSize;
    private boolean wholeAccommodationOnly;

    // Getters and Setters...
  }

  @AllArgsConstructor
  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  @Getter
  public static class RoomsCounts {
    private int bathroomsCount;
    private int kitchensCount;
    private int toiletsCount;

    // Getters and Setters...
  }

}
