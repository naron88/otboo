package com.otbooalone.module.location.exception;

import java.util.UUID;

public class LocationNotFoundException extends LocationException {

  public LocationNotFoundException() {
    super(LocationErrorCode.LOCATION_NOT_FOUND);
  }

  public static LocationNotFoundException withId(UUID id) {
    LocationNotFoundException exception = new LocationNotFoundException();
    exception.addDetail("id", id);
    return exception;
  }

}

