package com.cabinparser.web.responses;

import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class CabinResponse {

  private Integer id;
  private String name;
  private String description;
  private String urlFragment;
  private String vendorUniqueId; // we want this to be string as each vendor can have different namings
  private String type;          // Type of the cabin (e.g., luxury, standard, etc.)
  private int bedroomsCount;        // Number of rooms in the cabin
  private int regularSleepingBeds;        // number of regular beds
  private int extraSleepingBeds;        // number of extra sleeping beds (e.g. couch)
  private int maxPerson;        // Maximum number of persons allowed in the cabin
  private double rating;        // Cabin rating (e.g., 4.5 stars)
  private double reviewsCount;        // Cabin rating (e.g., 4.5 stars)
  private BigDecimal avgPricePerNight;  // Average price per night for renting the cabin
  private String priceType;  // Price type
  private double gpsPositionLatitude; // GPS coordinates of the cabin
  private double gpsPositionLongitude;
  private String region;
  private String district;
  private String locality;
  private List<String> images;
  private BigDecimal occupancy;
  private List<String> attributes;
}
