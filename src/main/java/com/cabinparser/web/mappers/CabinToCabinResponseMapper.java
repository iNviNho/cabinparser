package com.cabinparser.web.mappers;

import com.cabinparser.domain.cabin.Cabin;
import com.cabinparser.domain.cabin.CabinAttributes;
import com.cabinparser.web.responses.CabinResponse;
import java.util.List;
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

  default List<String> toAttributes(final List<CabinAttributes> attributes) {
    return attributes.stream().map(CabinAttributes::translation).sorted().toList();
  }
}
