package com.cabinparser.infrastructure.api.topreality;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;
import javax.validation.constraints.NotNull;

@Client("topreality-api-client")
public interface TopRealityApiClient {

  @Get("/vyhladavanie-nehnutelnosti-{page}.html?form=1&type%5B%5D={type}&searchType=string&distance=&q=&cena_od=&cena_do=&vymera_od=0&vymera_do=0&n_search=search&page=estate&obec=c500-%C5%BDilinsk%C3%BD+kraj%2Cc600-Banskobystrick%C3%BD+kraj%2Cc700-Pre%C5%A1ovsk%C3%BD+kraj&gpsPolygon=")
  String getCabinsForSale(@NotNull final int page, @NotNull final int type);

}
