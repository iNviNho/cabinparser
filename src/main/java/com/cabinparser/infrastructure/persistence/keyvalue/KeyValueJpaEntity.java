package com.cabinparser.infrastructure.persistence.keyvalue;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.data.annotation.MappedEntity;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Introspected
@MappedEntity(value = "key_value_store")
class KeyValueJpaEntity {

  @Id
  private String key;
  private String value;
}
