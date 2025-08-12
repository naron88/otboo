package com.otbooalone.module.auth.exception.common;

import com.otbooalone.module.auth.exception.AuthErrorCode;
import com.otbooalone.module.auth.exception.AuthException;

public class MissingRefreshTokenException extends AuthException {

  public MissingRefreshTokenException() {
    super(AuthErrorCode.REFRESH_TOKEN_MISSING);
  }

  public static MissingRefreshTokenException noDetail() {
    return new MissingRefreshTokenException();
  }
}
