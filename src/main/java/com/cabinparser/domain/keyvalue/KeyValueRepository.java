package com.cabinparser.domain.keyvalue;

import java.util.Optional;

public interface KeyValueRepository {

  void insert(final KeyValue keyValue);
  void update(final KeyValue keyValue);

  Optional<KeyValue> getById(final String key);
}
