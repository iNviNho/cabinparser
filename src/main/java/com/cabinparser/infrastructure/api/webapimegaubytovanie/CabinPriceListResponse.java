package com.cabinparser.infrastructure.api.webapimegaubytovanie;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CabinPriceListResponse(
  OffsetDateTime lastModifiedWhen,
  String checksum,
  String language,
  PriceList priceList
) {
  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public record PriceList(
    List<Season> seasons,
    List<Discount> discounts,
    String priceAlsoContains,
    List<ChargeableItem> chargeableItems,
    List<VisitorType> preferredVisitorTypes,
    List<VisitorType> rejectedVisitorTypes,
    List<Object> priceListsUploaded,
    boolean depositRequired,
    String depositType,
    Integer depositValue,
    Object customDepositValue,
    String depositNotes,
    Object pricingNotes,
    CancelConditions cancelConditions
  ) {
  }

  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public record Season(
    String name,
    String seasonType,
    LocalDate fromDate,
    LocalDate toDate,
    Integer fromDayNumber,
    Integer toDayNumber,
    List<PriceListItem> priceListItems,
    Integer seasonDefinitionId,
    Integer id,
    String bookingStrategy,
    List<UnitSeason> unitsSeasons
  ) {
  }

  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public record UnitSeason(
    int accommodationUnitId,
    String name,
    String seasonType,
    LocalDate fromDate,
    LocalDate toDate,
    Integer fromDayNumber,
    Integer toDayNumber,
    List<PriceListItem> priceListItems,
    Integer seasonDefinitionId,
    Integer id,
    List<PriceListItem> unitsSeasons
  ) {
  }

  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public record PriceListItem(
    Integer minPrice,
    Integer maxPrice,
    String currency,
    boolean isExactPrice,
    Object legacyPrice,
    Object note,
    Object priceValidFromDate,
    Object priceValidToDate,
    Integer id,
    String itemType,
    Integer validFromNightsCount,
    Integer validFromPersonsCount,
    List<Object> additionalPriceListItems
  ) {
  }

  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public record Discount(
    Integer id,
    Category category,
    Object subcategory,
    Integer discountPercent,
    Object exactDiscount,
    Object exactDiscountCurrency,
    Object minChildAge,
    Object maxChildAge,
    Object childWithoutBed,
    Object minPersonCount,
    Object maxPersonCount,
    Integer minNightsCount,
    Object maxNightsCount,
    Object minDaysBeforeArrival,
    Object maxDaysBeforeArrival,
    Object minVisitsCount,
    Object attraction,
    Object customAttractionName,
    String description
  ) {
  }

  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public record Category(
    Integer id,
    String name,
    boolean active,
    String identifier
  ) {
  }

  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public record ChargeableItem(
    ItemType itemType,
    String chargeType,
    String priceType,
    Integer priceFrom,
    Integer priceTo,
    Integer minimalAge,
    String currency,
    PricePerUnit pricePerUnit,
    String notes,
    Integer id
  ) {
  }

  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public record ItemType(
    Integer id,
    String name,
    boolean active,
    String identifier
  ) {
  }

  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public record PricePerUnit(
    Integer id,
    String name,
    boolean active,
    String identifier
  ) {
  }

  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public record VisitorType(
    Integer id,
    String name,
    boolean active,
    String featuresGroup,
    String shortName,
    String identifier
  ) {
  }

  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public record CancelConditions(
    String cancelConditions,
    Object cancelConditionsFileCaption,
    Object cancelConditionFile
  ) {
  }
}
