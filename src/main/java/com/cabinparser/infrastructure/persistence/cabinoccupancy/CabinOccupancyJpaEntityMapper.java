package com.cabinparser.infrastructure.persistence.cabinoccupancy;

import com.cabinparser.domain.cabinocupancy.CabinOccupancy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(
  componentModel = MappingConstants.ComponentModel.JSR330,
  unmappedSourcePolicy = ReportingPolicy.IGNORE,
  unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface CabinOccupancyJpaEntityMapper {

  CabinOccupancyJpaEntityMapper INSTANCE = Mappers.getMapper(CabinOccupancyJpaEntityMapper.class);

  CabinOccupancyJpaEntity toJpaEntity(CabinOccupancy cabin);

  CabinOccupancy toDomain(CabinOccupancyJpaEntity entity);

}
