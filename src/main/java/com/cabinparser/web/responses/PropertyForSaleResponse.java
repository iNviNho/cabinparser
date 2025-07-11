package com.cabinparser.web.responses;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class PropertyForSaleResponse {

  private UUID id;
  private String title;
  private String description;
  private BigDecimal price;
  private double gpsPositionLatitude; // GPS coordinates of the cabin
  private double gpsPositionLongitude;
  private String region;
  private String district;
  private String locality;
  private BigDecimal houseEstate;
  private BigDecimal floorEstate;
  private BigDecimal estate;
  private List<String> images;
  private String link;
  private boolean star;
  private String category;
}
