package com.otbooalone.module.auth.exception.common;

import com.otbooalone.module.auth.exception.AuthErrorCode;
import com.otbooalone.module.auth.exception.AuthException;

public class InvalidTokenException extends AuthException {

  public InvalidTokenException() {
    super(AuthErrorCode.INVALID_TOKEN);
  }

  public static InvalidTokenException noDetail() {
    return new InvalidTokenException();
  }
}
