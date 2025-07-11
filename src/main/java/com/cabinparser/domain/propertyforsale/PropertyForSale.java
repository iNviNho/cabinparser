package com.cabinparser.domain.propertyforsale;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class PropertyForSale {
    private UUID id; // UUID
    private String title;
    private String description;
    private BigDecimal price;
    private String locality;
    private String district;
    private String region;
    private String country;
    private Instant createdAt;
    private Instant updatedAt;
    private BigDecimal houseEstate;
    private BigDecimal floorEstate;
    private BigDecimal estate;
    private Double gpsPositionLatitude;
    private Double gpsPositionLongitude;
    private List<String> pictures; // JSONB as a list of image URLs
    private String link;
    private Instant deletedAt;
    private boolean star;
    private String category;
}