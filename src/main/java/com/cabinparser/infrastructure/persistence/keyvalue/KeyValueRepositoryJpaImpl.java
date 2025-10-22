package com.cabinparser.infrastructure.persistence.keyvalue;

import com.cabinparser.domain.keyvalue.KeyValue;
import com.cabinparser.domain.keyvalue.KeyValueRepository;
import jakarta.inject.Singleton;
import java.util.Optional;
import lombok.AllArgsConstructor;

@Singleton
@AllArgsConstructor
public class KeyValueRepositoryJpaImpl implements KeyValueRepository {

  private final KeyValueJpaRepository keyValueJpaRepository;
  private final KeyValueJpaEntityMapper keyValueJpaEntityMapper;

  @Override
  public void insert(KeyValue keyValue) {
    keyValueJpaRepository.save(
        keyValueJpaEntityMapper.toJpaEntity(keyValue)
    );
  }

  @Override
  public void update(KeyValue keyValue) {
    keyValueJpaRepository.update(
        keyValueJpaEntityMapper.toJpaEntity(keyValue)
    );
  }

  @Override
  public Optional<KeyValue> getById(String key) {
    return keyValueJpaRepository.findById(key)
        .map(keyValueJpaEntityMapper::toDomain);
  }
}
