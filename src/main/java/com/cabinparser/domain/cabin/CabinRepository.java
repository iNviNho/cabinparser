package com.cabinparser.domain.cabin;

import java.util.List;
import java.util.Optional;

public interface CabinRepository {

  Optional<Cabin> getByVendorAndVendorUniqueIdAndName(final String vendor, final String vendorUniqueId,
                                                      final String name);

  List<Cabin> getByVendor(final String vendor);

  void store(final Cabin cabin);

  void update(final Cabin cabin);

  Cabin getById(final int cabinId);
}
