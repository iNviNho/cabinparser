package com.cabinparser.application;

import io.micronaut.data.exceptions.DataAccessException;

public class Utils {

  private Utils() {
  }

  public static boolean isDuplicateKeyException(final DataAccessException e) {
    // Optionally, you can implement specific logic to check if the exception is related to a duplicate key
    return e.getMessage().contains("duplicate key") || e.getMessage().contains("Unique constraint violation");
  }
}
