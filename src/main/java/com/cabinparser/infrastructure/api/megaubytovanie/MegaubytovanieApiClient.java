package com.cabinparser.infrastructure.api.megaubytovanie;

import io.micronaut.http.annotation.Get;

@io.micronaut.http.client.annotation.Client("megaubytovanie-client")
public interface MegaubytovanieApiClient {
  @Get("/_next/data/4a31xRc3ToajZzMc5eLIL/sk/vysledky-vyhladavania.json" +
    "?" +
    "location={location}&" +
    "locationType={locationType}&" +
    "locationName={locationName}&" +
    "p={page}&" +
    "accommodationTypeNamedFilterIds[]={accomodationFilterIds}"
  )
  AccommodationSearchResponse getCabinsBasedOnLocation(
    String location,
    String locationType,
    String locationName,
    String page,
    String accomodationFilterIds
  );

  @Get("/_next/data/4a31xRc3ToajZzMc5eLIL/sk/vysledky-vyhladavania.json" +
    "?" +
    "p={page}&" +
    "accommodationTypeNamedFilterIds[]={accomodationFilterIds}"
  )
  AccommodationSearchResponse getCabins(
    int page,
    String accomodationFilterIds
  );


}
