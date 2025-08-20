package com.otbooalone.module.location.exception;

import com.otbooalone.global.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum LocationErrorCode implements ErrorCode{

  LOCATION_NOT_FOUND(HttpStatus.NOT_FOUND, "위치 정보를 찾을 수 없습니다.");

  private final HttpStatus status;
  private final String message;

  LocationErrorCode(HttpStatus status, String message) {
    this.status = status;
    this.message = message;
  }

  @Override
  public HttpStatus getHttpStatus() {
    return this.status;
  }

  @Override
  public String getMessage() {
    return this.message;
  }
}

