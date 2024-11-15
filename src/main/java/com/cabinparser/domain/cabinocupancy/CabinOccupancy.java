package com.cabinparser.domain.cabinocupancy;

import java.time.Instant;
import java.time.LocalDate;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CabinOccupancy {

  private int id;
  @NonNull
  private LocalDate date;

  @NonNull
  private Integer cabin_id;

  @NonNull
  private Instant created_at;
}
