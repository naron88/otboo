package com.otbooalone.module.auth.exception.common;

import com.otbooalone.module.auth.exception.AuthErrorCode;
import com.otbooalone.module.auth.exception.AuthException;

public class AuthenticationRequiredException extends AuthException {

  public AuthenticationRequiredException() {
    super(AuthErrorCode.AUTHENTICATION_REQUIRED);
  }

  public static AuthenticationRequiredException noDetail() {
    return new AuthenticationRequiredException();
  }
}
