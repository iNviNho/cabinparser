package com.cabinparser.infrastructure.api.webapimegaubytovanie;

import java.util.List;

public record AccommodationDetailResponse(
  String last_modified_when,
  String checksum,
  String language,
  AccommodationDetail accommodation_detail,
  String portal_season,
  List<RelatedLocation> related_locations
) {

  public record RelatedLocation(
    String name,
    String url_fragment,
    int url_fragment_id,
    String web_identifier,
    int id,
    String name_locative,
    String location_type,
    int accommodations_count,
    int attractions_count,
    String identifier
  ) {
  }

  public record AccommodationDetail(
    String name,
    boolean vip,
    boolean top,
    String created_when,
    BasicStats basic_stats,
    Address address,
    Header header,
    ImageGallery image_gallery,
    VideoGallery video_gallery,
    Contact contact,
    MainContent main_content,
    PropertiesAndEquipment properties_and_equipment,
    RatingAndReviews rating_and_reviews,
    boolean multiple_accommodated_groups_allowed,
    AccommodationPriceRange accommodation_price_range,
    List<AccommodationUnit> accommodation_units,
    int min_accommodated_guests,
    int max_accommodated_guests,
    int minimal_nights_stay,
    int hotel_stars_count,
    boolean published,
    boolean verified,
    boolean redirect_detail_to_listing,
    String condition_status,
    int id
  ) {
  }

  public record BasicStats(
    // Define fields for basic_stats
  ) {
  }

  public record Address(
    // Define fields for address
  ) {
  }

  public record Header(
    // Define fields for header
  ) {
  }

  public record ImageGallery(
    int exterior_images_count,
    int surroundings_images_count,
    int images_without_tags_count,
    List<Image> images
  ) {
    public record Image(
      // Define fields for image
    ) {
    }
  }

  public record VideoGallery(
    List<Video> videos
  ) {
    public record Video(
      // Define fields for video
    ) {
    }
  }

  public record Contact(
    String contact_name,
    OwnerPresenceType owner_presence_type,
    String phone_number_prefix,
    List<SocialNetworkAccount> social_network_accounts
  ) {
    public record OwnerPresenceType(
      // Define fields for owner_presence_type
    ) {
    }

    public record SocialNetworkAccount(
      // Define fields for social_network_account
    ) {
    }
  }

  public record MainContent(
    Catchphrase catchphrase,
    IconsBar icons_bar,
    Promotion promotion,
    boolean show_cooperation_since_enabled
  ) {
    public record Catchphrase(
      // Define fields for catchphrase
    ) {
    }

    public record IconsBar(
      // Define fields for icons_bar
    ) {
    }

    public record Promotion(
      String show_promotion_from_date,
      String show_promotion_to_date,
      Description description
    ) {
      public record Description(
        // Define fields for description
      ) {
      }
    }
  }

  public record PropertiesAndEquipment(
    SharedSpaces shared_spaces,
    Equipment equipment,
    AdditionalInformation additional_information,
    Location location
  ) {
    public record SharedSpaces(
      // Define fields for shared_spaces
    ) {
    }

    public record Equipment(
      // here
      List<Item> equipments_for_children,
      List<Item> exterior_equipments,
      List<Item> relax_opportunities,
      List<Item> sport_opportunities,
      List<Item> agro_tourism_options,
      List<Item> social_spaces,
      List<Item> additional_services
    ) {
      public record Item(
        String name,
        boolean active,
        String short_name,
        String identifier
      ) {
      }
    }

    public record AdditionalInformation(
      // Define fields for additional_information
    ) {
    }

    public record Location(
      // Define fields for location
    ) {
    }
  }

  public record RatingAndReviews(
    boolean reviews_enabled,
    Rating rating,
    int active_reviews_count,
    String ratings_range_name,
    List<TopReview> top_reviews,
    Integer nth_best_rated_accommodation,
    int same_accommodation_type_accommodations_count
  ) {
    public record Rating(
      // Define fields for rating
    ) {
    }

    public record TopReview(
      // Define fields for top_review
    ) {
    }
  }

  public record AccommodationPriceRange(
    int price_from,
    int price_to,
    String currency,
    String price_type
  ) {
  }

  public record AccommodationUnit(
    // Define fields for accommodation_unit
  ) {
  }
}
