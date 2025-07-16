package com.cabinparser.application.schedulers;

import com.cabinparser.application.schedulers.sub.FetchAndProcessTopRealityCabinsForSale;
import com.cabinparser.application.schedulers.sub.FifthCalculateAveragePricePerNightScheduler;
import com.cabinparser.application.schedulers.sub.FirstFetchAndProcessMegaubytovanieCabinsJob;
import com.cabinparser.application.schedulers.sub.FourthOccupancyCalculateScheduler;
import com.cabinparser.application.schedulers.sub.SecondUpdateMegaubytovanieCabinOccupancyJob;
import com.cabinparser.application.schedulers.sub.SixthUpdateCabinAttributesScheduler;
import com.cabinparser.application.schedulers.sub.ThirdUpdateDistrictRegionAndLocalityOfAllData;
import com.cabinparser.application.schedulers.sub.UpdateAvailabilityOfPropertiesForSale;
import com.cabinparser.application.schedulers.sub.UpdatePriceDiffsForPropertiesForSale;
import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Singleton;
import lombok.AllArgsConstructor;

@Singleton
@AllArgsConstructor
public class MainScheduler {

  private final UpdateAvailabilityOfPropertiesForSale updateAvailabilityOfPropertiesForSale;
  protected FirstFetchAndProcessMegaubytovanieCabinsJob firstFetchAndProcessMegaubytovanieCabinsJob;
  protected SecondUpdateMegaubytovanieCabinOccupancyJob secondUpdateMegaubytovanieCabinOccupancyJob;
  protected ThirdUpdateDistrictRegionAndLocalityOfAllData thirdUpdateDistrictRegionAndLocalityOfAllData;
  protected FourthOccupancyCalculateScheduler fourthOccupancyCalculateScheduler;
  protected FifthCalculateAveragePricePerNightScheduler fifthCalculateAveragePricePerNightScheduler;
  protected SixthUpdateCabinAttributesScheduler sixthUpdateCabinAttributesScheduler;
  protected FetchAndProcessTopRealityCabinsForSale fetchAndProcessTopRealityCabinsForSale;
  protected UpdatePriceDiffsForPropertiesForSale updatePriceDiffsForPropertiesForSale;

  @Scheduled(initialDelay = "0s", fixedDelay = "1d")
  public void start() throws InterruptedException {
    // fetchAndProcessTopRealityCabinsForSale.parse();
    // updatePriceDiffsForPropertiesForSale.parse();
    // updateAvailabilityOfPropertiesForSale.parse();

    // firstFetchAndProcessMegaubytovanieCabinsJob.parse();
    // secondUpdateMegaubytovanieCabinOccupancyJob.parse();
    // thirdUpdateDistrictRegionAndLocalityOfAllData.parse();
    // fourthOccupancyCalculateScheduler.parse();
    // fifthCalculateAveragePricePerNightScheduler.parse();
    // sixthUpdateCabinAttributesScheduler.process();
  }
}
