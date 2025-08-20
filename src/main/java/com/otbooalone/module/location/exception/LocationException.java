package com.otbooalone.module.location.exception;

import com.otbooalone.global.exception.BaseException;
import com.otbooalone.global.exception.ErrorCode;

public class LocationException extends BaseException {

  public LocationException(ErrorCode errorCode) {
    super(errorCode);
  }
}