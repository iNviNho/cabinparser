package com.cabinparser.infrastructure.persistence.cabin;

import com.cabinparser.domain.cabin.Cabin;
import com.cabinparser.domain.cabin.CabinAttributes;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
  componentModel = MappingConstants.ComponentModel.JSR330,
  unmappedSourcePolicy = ReportingPolicy.IGNORE,
  unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface CabinJpaEntityMapper {

  CabinJpaEntity toJpaEntity(Cabin cabin);

  Cabin toDomain(CabinJpaEntity entity);

  default String map(final List<String> value) {
    return String.join(",", value);
  }

  default List<String> map(final String value) {
    return Arrays.stream(value.split(",")).toList();
  }

  default String mapCabinAttributesToOneJson(final List<CabinAttributes> value) {
    final ObjectMapper objectMapper = new ObjectMapper();
    final String result;
    try {
      result = objectMapper.writeValueAsString(value);
    } catch (final JsonProcessingException e) {
      throw new RuntimeException(e);
    }
    return result;
  }

  default List<CabinAttributes> mapCabinAttributesFromJson(final String value) {
    final ObjectMapper objectMapper = new ObjectMapper();
    final List<CabinAttributes> result;
    if (value == null) {
      return null;
    }
    try {
      result = Arrays.asList(objectMapper.readValue(value, CabinAttributes[].class));
    } catch (final JsonProcessingException e) {
      throw new RuntimeException(e);
    }
    return result;
  }

}
