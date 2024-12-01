package com.cabinparser.domain.cabinocupancy;

import java.time.LocalDate;
import java.util.List;

public interface CabinOccupancyRepository {

  void store(final CabinOccupancy cabin);

  int getOccupiedDaysCabinIdAndDateRange(final int cabinId, final LocalDate startDate, final LocalDate endDate);

  List<CabinOccupancy> getCabinOccupanciesByCabinIdAndDateRange(final int cabinId, final LocalDate startDate,
                                                                final LocalDate endDate);

}
