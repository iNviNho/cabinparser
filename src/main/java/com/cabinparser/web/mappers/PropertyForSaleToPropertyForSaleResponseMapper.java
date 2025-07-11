package com.cabinparser.web.mappers;

import com.cabinparser.domain.propertyforsale.PropertyForSale;
import com.cabinparser.web.responses.PropertyForSaleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
  componentModel = MappingConstants.ComponentModel.JSR330,
  unmappedSourcePolicy = ReportingPolicy.IGNORE,
  unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface PropertyForSaleToPropertyForSaleResponseMapper {

  @Mapping(source = "pictures", target = "images")
  PropertyForSaleResponse toCabinResponse(PropertyForSale cabin);

}
