package com.otbooalone.global.exception;

import org.springframework.http.HttpStatus;

public enum CommonErrorCode implements ErrorCode {

  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),
  INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "잘못된 입력값입니다."),
  METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "잘못된 HTTP 메서드를 호출하였습니다."),
  INVALID_JSON_FORMAT(HttpStatus.BAD_REQUEST, "잘못된 요청 형식입니다. 입력값을 확인하세요."),
  URI_NOT_FOUND(HttpStatus.NOT_FOUND, "지원하지 않는 URI 입니다.");

  private final HttpStatus status;
  private final String message;

  CommonErrorCode(HttpStatus status, String message) {
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
