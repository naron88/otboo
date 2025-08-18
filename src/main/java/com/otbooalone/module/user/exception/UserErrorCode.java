package com.otbooalone.module.user.exception;

import com.otbooalone.global.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum UserErrorCode implements ErrorCode {

  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
  EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 있는 이메일입니다.");

  private final HttpStatus status;
  private final String message;

  UserErrorCode(HttpStatus status, String message) {
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
