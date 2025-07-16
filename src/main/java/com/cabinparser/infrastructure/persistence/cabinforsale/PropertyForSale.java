package com.cabinparser.infrastructure.persistence.cabinforsale;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.TypeDef;
import io.micronaut.data.model.DataType;
import java.util.UUID;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Introspected
@MappedEntity(value = "properties_for_sale")
public class PropertyForSale {
    @Id
    private UUID id; // UUID

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal price;

    private String locality;
    private String district;
    private String region;
    private String country;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "house_estate", precision = 8, scale = 2)
    private BigDecimal houseEstate;

    @Column(name = "floor_estate", precision = 8, scale = 2)
    private BigDecimal floorEstate;

    @Column(name = "estate", precision = 8, scale = 2)
    private BigDecimal estate;

    @Column(name = "gps_position_latitude")
    private Double gpsPositionLatitude;

    @Column(name = "gps_position_longitude")
    private Double gpsPositionLongitude;

    @TypeDef(type = DataType.JSON)
    @Column(name = "pictures")
    private String pictures; // JSONB as a list of image URLs

    @Column(name = "link")
    private String link;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    private boolean star;

    private String category;
    @Column(name = "region_price_to_average_difference")
    private BigDecimal regionPriceToAverageDifference;
    @Column(name = "district_price_to_average_difference")
    private BigDecimal districtPriceToAverageDifference;
    @Column(name = "locality_price_to_average_difference")
    private BigDecimal localityPriceToAverageDifference;

}