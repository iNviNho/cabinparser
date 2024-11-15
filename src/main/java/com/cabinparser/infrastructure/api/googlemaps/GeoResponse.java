package com.cabinparser.infrastructure.api.googlemaps;

import java.util.List;

public record GeoResponse(
  PlusCode plus_code,
  List<Result> results,
  String status
) {

  public record PlusCode(
    String compound_code,
    String global_code
  ) {
  }

  public record Result(
    List<AddressComponent> address_components,
    String formatted_address,
    Geometry geometry,
    String place_id,
    List<String> types,
    PlusCode plus_code // Optional, as it may not always be present
  ) {
  }

  public record AddressComponent(
    String long_name,
    String short_name,
    List<String> types
  ) {
  }

  public record Geometry(
    Bounds bounds,
    Location location,
    String location_type,
    Viewport viewport
  ) {
  }

  public record Bounds(
    Coordinate northeast,
    Coordinate southwest
  ) {
  }

  public record Coordinate(
    double lat,
    double lng
  ) {
  }

  public record Location(
    double lat,
    double lng
  ) {
  }

  public record Viewport(
    Coordinate northeast,
    Coordinate southwest
  ) {
  }
}

