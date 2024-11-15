package com.cabinparser.infrastructure.api.megaubytovanie;

import java.util.List;

public class AccommodationSearchResponse {

  public PageProps pageProps;
  public boolean __N_SSP;

  public static class PageProps {
    public AccommodationsSearchData accommodationsSearchData;
    public String countryCode;
    public String locationType;
    public int location;
    public String locationName;
    public String locationNameLocative;
    public String locationSlug;
    public String _sentryTraceData;
    public String _sentryBaggage;
  }

  public static class AccommodationsSearchData {
    public String last_modified_when;
    public String checksum;
    public String language;
    public int total_records_count;
    public List<Accommodation> accommodations;
  }

  public static class Accommodation {
    public String name;
    public String url_fragment;
    public int url_fragment_id;
    public String web_identifier;
    public int id;
    public GpsPosition gps_position;
    public int distance_meters;
    public boolean vip;
    public boolean top;
    public String created_when;
    public String last_calendar_update_when;
    public boolean multiple_accommodated_groups_allowed;
    public BasicStats basic_stats;
    public CityLink city_link;
    public Object city_part_link;
    public int distance_meters_from_city;
    public Object custom_city_part;
    public Placement placement;
    public List<PlacementDetail> placement_details;
    public List<Image> images;
    public RatingAndReviews rating_and_reviews;
    public AccommodationPriceRange accommodation_price_range;
    public String description;
    public String catchphrase;
    public String promotion_text;
    public Object show_promotion_from_date;
    public Object show_promotion_to_date;
    public Object hotel_stars_count;
    public List<AccommodationUnit> accommodation_units;
    public List<Object> shared_rooms;
  }

  public static class GpsPosition {
    public double latitude;
    public double longitude;
  }

  public static class BasicStats {
    public int regular_beds_sleeping_capacity;
    public int extra_beds_sleeping_capacity;
    public int bedrooms_count;
    public int toilets_count;
    public int bathrooms_count;
  }

  public static class CityLink {
    public String name;
    public String url_fragment;
    public int url_fragment_id;
    public String web_identifier;
    public int id;
    public String name_locative;
    public String location_type;
    public int accommodations_count;
    public int attractions_count;
    public String identifier;
  }

  public static class Placement {
    public int id;
    public String name;
    public boolean active;
    public String features_group;
    public String short_name;
    public String identifier;
  }

  public static class PlacementDetail {
    public int id;
    public String name;
    public boolean active;
    public String features_group;
    public String short_name;
    public String identifier;
  }

  public static class Image {
    public String url;
    public int file_size;
    public String mime_type;
    public int width;
    public int height;
    public String alt;
    public String caption;
    public boolean interior_image;
    public boolean exterior_image;
    public boolean surroundings_image;
    public boolean main_image_for_winter;
    public boolean main_image_for_summer;
    public int id;
  }

  public static class RatingAndReviews {
    public boolean reviews_enabled;
    public Rating rating;
    public int active_reviews_count;
    public String ratings_range_name;
  }

  public static class Rating {
    public double cleanliness;
    public double equipment;
    public double services;
    public double personnel;
    public double location;
    public double activities;
    public double price_quality_ratio;
    public double average;
  }

  public static class AccommodationPriceRange {
    public int price_from;
    public int price_to;
    public String currency;
    public String price_type;
  }

  public static class AccommodationUnit {
    public String name;
    public String custom_name;
    public UnitType unit_type;
    public Object owner_defined_beds_count;
    public int units_count;
    public int bedrooms_count;
    public int sleeping_capacity;
    public List<OtherRoomCount> other_rooms_counts;
    public List<RegularBed> regular_beds;
    public List<ExtraBed> extra_beds;
    public int id;
  }

  public static class UnitType {
    public int id;
    public String name;
    public boolean active;
    public String identifier;
    public boolean show_bedrooms_count_by_name;
    public boolean show_beds_count_by_name;
    public String name_locative;
  }

  public static class OtherRoomCount {
    public RoomSubtype room_subtype;
    public int rooms_count;
  }

  public static class RoomSubtype {
    public int id;
    public String name;
    public boolean active;
    public String identifier;
  }

  public static class RegularBed {
    public int same_beds_count;
    public int single_bed_sleeping_capacity;
    public Object outside_room_location;
    public RegularBedType regular_bed_type;
    public boolean merge_possible;
    public boolean split_possible;
  }

  public static class RegularBedType {
    public int id;
    public String name;
    public boolean active;
    public String features_group;
    public String short_name;
    public String identifier;
  }

  public static class ExtraBed {
    public int same_beds_count;
    public int single_bed_sleeping_capacity;
    public Object outside_room_location;
    public ExtraBedType extra_bed_type;
  }

  public static class ExtraBedType {
    public int id;
    public String name;
    public boolean active;
    public String features_group;
    public String short_name;
    public String identifier;
  }
}
