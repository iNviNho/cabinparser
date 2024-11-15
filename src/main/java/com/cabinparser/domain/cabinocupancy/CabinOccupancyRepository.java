package com.cabinparser.domain.cabinocupancy;

import java.time.LocalDate;

public interface CabinOccupancyRepository {

  void store(final CabinOccupancy cabin);

  int getOccupanciesByCabinIdAndDateRange(final int cabinId, final LocalDate startDate, final LocalDate endDate);

}
