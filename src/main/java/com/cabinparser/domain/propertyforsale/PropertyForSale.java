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
    private BigDecimal regionPriceToAverageDifference;
    private BigDecimal districtPriceToAverageDifference;
    private BigDecimal localityPriceToAverageDifference;

    public BigDecimal getPricePerSquareMeterForLand() {
        final var estateForLand = getEstateForLand();
        if (estateForLand == null) {
            return null;
        }
        return estateForLand.compareTo(BigDecimal.ZERO) > 0
            ? price.divide(estateForLand, BigDecimal.ROUND_HALF_UP)
            : BigDecimal.ZERO;
    }

    public BigDecimal getPricePerSquareMeterForHouse() {
        final var estateForHouse = getEstateForHouse();
        if (estateForHouse == null) {
            return null;
        }
        return estateForHouse.compareTo(BigDecimal.ZERO) > 0
            ? price.divide(estateForHouse, BigDecimal.ROUND_HALF_UP)
            : BigDecimal.ZERO;
    }

    public BigDecimal getEstateForLand() {
        if (estate != null && estate.compareTo(BigDecimal.ZERO) > 0) {
            return estate;
        } else if (floorEstate != null && floorEstate.compareTo(BigDecimal.ZERO) > 0) {
            return floorEstate;
        } else if (houseEstate != null && houseEstate.compareTo(BigDecimal.ZERO) > 0) {
            return houseEstate;
        }
        return null;
    }

    public BigDecimal getEstateForHouse() {
        // we only want to count in estate and floor estate
        if (estate != null && estate.compareTo(BigDecimal.ZERO) > 0 && floorEstate != null && floorEstate.compareTo(BigDecimal.ZERO) > 0) {
            return estate.add(floorEstate);
        }
        return null;
    }

    public boolean hasSaneEstateForLand() {
        // check if values are either 0 or greater than 100
        return getEstateForLand() != null && getEstateForLand().compareTo(new BigDecimal(100)) > 0;
    }

    public boolean hasSaneEstateForHouse() {
        // check if values are either 0 or greater than 100
        return getEstateForHouse() != null && getEstateForHouse().compareTo(new BigDecimal(100)) > 0;
    }
}