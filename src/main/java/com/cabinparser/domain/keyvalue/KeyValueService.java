package com.cabinparser.domain.keyvalue;

import jakarta.inject.Singleton;
import java.util.Optional;
import lombok.AllArgsConstructor;

@Singleton
@AllArgsConstructor
public class KeyValueService {
  public final KeyValueRepository keyValueRepository;

  public void insert(final KeyValue keyValue) {
    keyValueRepository.insert(keyValue);
  }

  public void update(final KeyValue keyValue) {
    keyValueRepository.update(keyValue);
  }

  public Optional<KeyValue> getById(final String key) {
    return keyValueRepository.getById(key);
  }

}
