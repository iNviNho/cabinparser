package com.cabinparser.infrastructure.persistence.keyvalue;

import com.cabinparser.domain.keyvalue.KeyValue;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(
  componentModel = MappingConstants.ComponentModel.JSR330
)
public interface KeyValueJpaEntityMapper {

  KeyValueJpaEntity toJpaEntity(KeyValue keyValue);

  KeyValue toDomain(KeyValueJpaEntity entity);


}
