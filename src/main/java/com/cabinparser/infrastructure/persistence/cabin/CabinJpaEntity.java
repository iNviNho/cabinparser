package com.cabinparser.infrastructure.persistence.cabin;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.TypeDef;
import io.micronaut.data.model.DataType;
import java.math.BigDecimal;
import java.time.Instant;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Introspected
@MappedEntity(value = "cabins")
class CabinJpaEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String vendorUniqueId;
  private String name;
  private String urlFragment;
  private String type;          // Type of the cabin (e.g., luxury, standard, etc.)
  private int bedroomsCount;    // Number of rooms in the cabin
  private int regularSleepingBeds;    // Number of regular beds
  private int extraSleepingBeds;    // Number of extra sleeping beds (e.g., couch)
  private int maxPerson;    // Maximum number of persons allowed in the cabin
  private double rating;    // Cabin rating (e.g., 4.5 stars)
  private double reviewsCount;    // Number of reviews for the cabin
  private BigDecimal avgPricePerNight;  // Average price per night for renting the cabin
  private String priceType;  // Price type
  private double gpsPositionLatitude; // GPS coordinates of the cabin
  private double gpsPositionLongitude; // GPS coordinates of the cabin
  private String description;   // Textual description of the cabin
  private String vendor;    // Vendor from where the cabin information was fetched (e.g., API, website)
  private String images;  // List of URLs for the cabin's images
  private Instant lastCalendarUpdate;
  private Instant createdWhen;
  @TypeDef(type = DataType.JSON)
  private String searchDump;
  private Instant createdAt;
  private String district;
  private String region;
  private String country;
  @TypeDef(type = DataType.JSON)
  private String geocodingDump;
  private String locality;
  private Instant lastCalendarOccupancyUpdate;
  private BigDecimal occupancy;
  @TypeDef(type = DataType.JSON)
  private String attributes;
  private boolean star;
}
