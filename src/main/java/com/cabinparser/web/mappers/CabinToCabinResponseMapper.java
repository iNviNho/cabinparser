package com.cabinparser.web.mappers;

import com.cabinparser.domain.cabin.Cabin;
import com.cabinparser.web.responses.CabinResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
  componentModel = MappingConstants.ComponentModel.JSR330,
  unmappedSourcePolicy = ReportingPolicy.IGNORE,
  unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface CabinToCabinResponseMapper {

  CabinResponse toCabinResponse(Cabin cabin);
}
