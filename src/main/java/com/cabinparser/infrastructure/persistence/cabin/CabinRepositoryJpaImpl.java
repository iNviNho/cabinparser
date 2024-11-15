package com.cabinparser.infrastructure.persistence.cabin;

import com.cabinparser.domain.cabin.Cabin;
import com.cabinparser.domain.cabin.CabinRepository;
import jakarta.inject.Singleton;
import java.util.List;
import java.util.Optional;

@Singleton
public class CabinRepositoryJpaImpl implements CabinRepository {

  private final CabinJpaRepository cabinJpaRepository;
  private final CabinJpaEntityMapper cabinJpaEntityMapper;

  public CabinRepositoryJpaImpl(final CabinJpaRepository cabinJpaRepository,
                                final CabinJpaEntityMapper cabinJpaEntityMapper) {
    this.cabinJpaRepository = cabinJpaRepository;
    this.cabinJpaEntityMapper = cabinJpaEntityMapper;
  }

  @Override
  public void store(final Cabin cabin) {
    this.cabinJpaEntityMapper.toDomain(
      this.cabinJpaRepository.save(
        cabinJpaEntityMapper.toJpaEntity(cabin)
      )
    );
  }

  @Override
  public void update(final Cabin cabin) {
    this.cabinJpaEntityMapper.toDomain(
      this.cabinJpaRepository.update(
        cabinJpaEntityMapper.toJpaEntity(cabin)
      )
    );
  }

  @Override
  public List<Cabin> getByVendor(final String vendor) {
    return cabinJpaRepository.findByVendor(vendor)
      .stream()
      .map(cabinJpaEntityMapper::toDomain)
      .toList();
  }

  @Override
  public Optional<Cabin> getByVendorAndVendorUniqueIdAndName(final String vendor, final String vendorUniqueId,
                                                             final String name) {
    return cabinJpaRepository.findByVendorAndVendorUniqueIdAndName(vendor, vendorUniqueId, name)
      .map(cabinJpaEntityMapper::toDomain);
  }

}
