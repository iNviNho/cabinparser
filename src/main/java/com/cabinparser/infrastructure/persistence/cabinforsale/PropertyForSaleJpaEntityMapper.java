package com.cabinparser.infrastructure.persistence.cabinforsale;

import com.cabinparser.domain.propertyforsale.PropertyForSale;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(
  componentModel = MappingConstants.ComponentModel.JSR330,
  unmappedSourcePolicy = ReportingPolicy.IGNORE,
  unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface PropertyForSaleJpaEntityMapper {

  PropertyForSaleJpaEntityMapper INSTANCE = Mappers.getMapper(PropertyForSaleJpaEntityMapper.class);

  com.cabinparser.infrastructure.persistence.cabinforsale.PropertyForSale toJpaEntity(PropertyForSale cabin);

  PropertyForSale toDomain(com.cabinparser.infrastructure.persistence.cabinforsale.PropertyForSale entity);

  default String mapListOfStringsToString(final List<String> value) {
    final ObjectMapper objectMapper = new ObjectMapper();
    final String result;
    try {
      result = objectMapper.writeValueAsString(value);
    } catch (final JsonProcessingException e) {
      throw new RuntimeException(e);
    }
    return result;
  }

  default List<String> mapStringToListOfStrings(final String value) {
    final ObjectMapper objectMapper = new ObjectMapper();
    final List<String> result;
    if (value == null) {
      return List.of();
    }
    try {
      result = objectMapper.readValue(value, new TypeReference<>() {});
    } catch (final JsonProcessingException e) {
      throw new RuntimeException(e);
    }
    return result;
  }

}
