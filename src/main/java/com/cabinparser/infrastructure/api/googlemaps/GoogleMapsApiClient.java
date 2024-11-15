package com.cabinparser.infrastructure.api.googlemaps;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;

@Client("google-maps-client")
public interface GoogleMapsApiClient {
  @Get("/maps/api/geocode/json" +
    "?" +
    "latlng={latlng}&" +
    "key={key}&" +
    "language=sk"
  )
  GeoResponse geoRequest(
    String latlng,
    String key
  );

}
