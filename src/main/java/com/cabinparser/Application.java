package com.cabinparser;

import io.micronaut.context.annotation.Context;
import io.micronaut.context.event.StartupEvent;
import io.micronaut.runtime.Micronaut;
import io.micronaut.runtime.event.ApplicationShutdownEvent;
import io.micronaut.runtime.event.annotation.EventListener;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Context
public class Application {

  public static void main(final String[] args) {
    Micronaut.run(Application.class, args);
  }

  @EventListener
  public void onStartup(final StartupEvent event) {
    log.info("Starting app");
  }

  @EventListener
  public void onShutdown(final ApplicationShutdownEvent event) {
    log.info("Stopping app");
  }

}