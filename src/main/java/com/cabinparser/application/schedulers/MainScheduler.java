package com.cabinparser.application.schedulers;

import com.cabinparser.application.schedulers.sub.FifthCalculateAveragePricePerNightScheduler;
import com.cabinparser.application.schedulers.sub.FirstFetchAndProcessMegaubytovanieCabinsJob;
import com.cabinparser.application.schedulers.sub.FourthOccupancyCalculateScheduler;
import com.cabinparser.application.schedulers.sub.SecondUpdateMegaubytovanieCabinOccupancyJob;
import com.cabinparser.application.schedulers.sub.SixthUpdateCabinAttributesScheduler;
import com.cabinparser.application.schedulers.sub.ThirdUpdateDistrictRegionAndLocalityOfCabins;
import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Singleton;
import lombok.AllArgsConstructor;

@Singleton
@AllArgsConstructor
public class MainScheduler {

  protected FirstFetchAndProcessMegaubytovanieCabinsJob firstFetchAndProcessMegaubytovanieCabinsJob;
  protected SecondUpdateMegaubytovanieCabinOccupancyJob secondUpdateMegaubytovanieCabinOccupancyJob;
  protected ThirdUpdateDistrictRegionAndLocalityOfCabins thirdUpdateDistrictRegionAndLocalityOfCabins;
  protected FourthOccupancyCalculateScheduler fourthOccupancyCalculateScheduler;
  protected FifthCalculateAveragePricePerNightScheduler fifthCalculateAveragePricePerNightScheduler;
  protected SixthUpdateCabinAttributesScheduler sixthUpdateCabinAttributesScheduler;

  // @Scheduled(initialDelay = "1s", fixedDelay = "1d")
  public void start() throws InterruptedException {
    firstFetchAndProcessMegaubytovanieCabinsJob.parse();
    secondUpdateMegaubytovanieCabinOccupancyJob.parse();
    thirdUpdateDistrictRegionAndLocalityOfCabins.parse();
    fourthOccupancyCalculateScheduler.parse();
    fifthCalculateAveragePricePerNightScheduler.parse();
    sixthUpdateCabinAttributesScheduler.process();
  }
}
