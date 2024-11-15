package com.cabinparser.infrastructure.api.webapimegaubytovanie;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record AccommodationSearchResponse(
  String lastModifiedWhen,
  String checksum,
  String language,
  int totalRecordsCount,
  List<Accommodation> accommodations
) {

  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public record Accommodation(
    String name,
    String urlFragment,
    int urlFragmentId,
    String webIdentifier,
    int id,
    GpsPosition gpsPosition,
    Integer distanceMeters,
    boolean vip,
    boolean top,
    String createdWhen,
    String lastCalendarUpdateWhen,
    boolean multipleAccommodatedGroupsAllowed,
    BasicStats basicStats,
    CityLink cityLink,
    CityPartLink cityPartLink,
    Integer distanceMetersFromCity,
    String customCityPart,
    Placement placement,
    List<PlacementDetails> placementDetails,
    List<Image> images,
    RatingAndReviews ratingAndReviews,
    AccommodationPriceRange accommodationPriceRange,
    String description,
    String catchphrase,
    String promotionText,
    String showPromotionFromDate,
    String showPromotionToDate,
    Integer hotelStarsCount,
    List<AccommodationUnit> accommodationUnits,
    List<SharedRoom> sharedRooms
  ) {

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record GpsPosition(double latitude, double longitude) {
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record BasicStats(
      int regularBedsSleepingCapacity,
      int extraBedsSleepingCapacity,
      int bedroomsCount,
      int toiletsCount,
      int bathroomsCount
    ) {
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record CityLink(
      String name,
      String urlFragment,
      int urlFragmentId,
      String webIdentifier,
      int id,
      String nameLocative,
      String locationType,
      int accommodationsCount,
      int attractionsCount,
      String identifier
    ) {
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record CityPartLink() {
    } // Assuming this is null based on your example

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record Placement(
      int id,
      String name,
      boolean active,
      String featuresGroup,
      String shortName,
      String identifier
    ) {
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record PlacementDetails(
      int id,
      String name,
      boolean active,
      String featuresGroup,
      String shortName,
      String identifier
    ) {
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record Image(
      String url,
      int fileSize,
      String mimeType,
      int width,
      int height,
      String alt,
      String caption,
      boolean interiorImage,
      boolean exteriorImage,
      boolean surroundingsImage,
      boolean mainImageForWinter,
      boolean mainImageForSummer,
      int id
    ) {
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record RatingAndReviews(
      boolean reviewsEnabled,
      Rating rating,
      int activeReviewsCount,
      String ratingsRangeName
    ) {

      @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
      public record Rating(
        double cleanliness,
        double equipment,
        double services,
        double personnel,
        double location,
        double activities,
        double priceQualityRatio,
        double average
      ) {
      }
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record AccommodationPriceRange(
      double priceFrom,
      double priceTo,
      String currency,
      String priceType
    ) {
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record AccommodationUnit(
      String name,
      String customName,
      UnitType unitType,
      int ownerDefinedBedsCount,
      int unitsCount,
      int bedroomsCount,
      int sleepingCapacity,
      List<OtherRoomCount> otherRoomsCounts,
      List<RegularBed> regularBeds,
      List<ExtraBed> extraBeds,
      int id
    ) {

      @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
      public record UnitType(
        int id,
        String name,
        boolean active,
        String identifier,
        boolean showBedroomsCountByName,
        boolean showBedsCountByName,
        String nameLocative
      ) {
      }

      @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
      public record RegularBed(
        int sameBedsCount,
        int singleBedSleepingCapacity,
        ExtraBed.OutsideRoomLocation outsideRoomLocation,
        RegularBedType regularBedType,
        boolean mergePossible,
        boolean splitPossible
      ) {

        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public record RegularBedType(
          int id,
          String name,
          boolean active,
          String featuresGroup,
          String shortName,
          String identifier
        ) {
        }
      }

      @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
      public record ExtraBed(
        int sameBedsCount,
        int singleBedSleepingCapacity,
        OutsideRoomLocation outsideRoomLocation,
        ExtraBedType extraBedType
      ) {

        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public record OutsideRoomLocation(
          int id,
          String name,
          boolean active,
          String identifier
        ) {
        }

        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public record ExtraBedType(
          int id,
          String name,
          boolean active,
          String featuresGroup,
          String shortName,
          String identifier
        ) {
        }
      }

      @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
      public record OtherRoomCount() {
      } // Assuming empty based on your example
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record SharedRoom(
      int sameRoomsCount,
      RoomType roomType,
      RoomSubtype roomSubtype,
      List<AccommodationUnit.RegularBed> regularBeds,
      List<AccommodationUnit.ExtraBed> extraBeds,
      int id
    ) {

      @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
      public record RoomType(
        int id,
        String name,
        boolean active,
        String identifier
      ) {
      }

      @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
      public record RoomSubtype(
        int id,
        String name,
        boolean active,
        String identifier
      ) {
      }
    }
  }
}
