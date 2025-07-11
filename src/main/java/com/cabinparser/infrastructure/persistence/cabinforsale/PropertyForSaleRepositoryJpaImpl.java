package com.cabinparser.infrastructure.persistence.cabinforsale;

import com.cabinparser.domain.propertyforsale.PropertyForSale;
import com.cabinparser.domain.propertyforsale.PropertyForSaleRepository;
import jakarta.inject.Singleton;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Singleton
@Slf4j
@AllArgsConstructor
public class PropertyForSaleRepositoryJpaImpl implements PropertyForSaleRepository {

  private final PropertyForSaleJpaRepository propertyForSaleJpaRepository;
  private final PropertyForSaleJpaEntityMapper propertyForSaleJpaEntityMapper;

  @Override
  public void store(final PropertyForSale propertyForSale) {
    this.propertyForSaleJpaRepository.save(
      propertyForSaleJpaEntityMapper.toJpaEntity(propertyForSale)
    );
  }

  @Override
  public List<PropertyForSale> getAllForSale() {
    return this.propertyForSaleJpaRepository.findAll()
      .stream()
      .map(propertyForSaleJpaEntityMapper::toDomain)
      .toList();
  }

  @Override
  public void update(PropertyForSale propertyForSale) {
    this.propertyForSaleJpaRepository.update(
      propertyForSaleJpaEntityMapper.toJpaEntity(propertyForSale)
    );
  }

  @Override
  public Optional<PropertyForSale> getByLink(String link) {
    return propertyForSaleJpaRepository.getByLink(link)
      .map(propertyForSaleJpaEntityMapper::toDomain);
  }
}
