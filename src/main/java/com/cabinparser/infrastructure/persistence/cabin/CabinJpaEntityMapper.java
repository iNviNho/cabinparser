package com.cabinparser.infrastructure.persistence.cabin;

import com.cabinparser.domain.cabin.Cabin;
import java.util.Arrays;
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
public interface CabinJpaEntityMapper {

  CabinJpaEntityMapper INSTANCE = Mappers.getMapper(CabinJpaEntityMapper.class);

  CabinJpaEntity toJpaEntity(Cabin cabin);

  Cabin toDomain(CabinJpaEntity entity);

  default String map(final List<String> value) {
    return String.join(",", value);
  }

  default List<String> map(final String value) {
    return Arrays.stream(value.split(",")).toList();
  }
}
