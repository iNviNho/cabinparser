package com.cabinparser.domain.propertyforsale;

import java.util.List;
import java.util.Optional;

public interface PropertyForSaleRepository {

  void store(final PropertyForSale propertyForSale);

  Optional<PropertyForSale> getByLink(String link);

  List<PropertyForSale> getAllForSale();

  void update(PropertyForSale propertyForSale);

  List<PropertyForSale> getAllForSaleByCategory(String category);
}
