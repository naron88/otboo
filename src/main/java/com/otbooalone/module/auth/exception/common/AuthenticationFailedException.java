package com.otbooalone.module.auth.exception.common;

import com.otbooalone.module.auth.exception.AuthErrorCode;
import com.otbooalone.module.auth.exception.AuthException;

public class AuthenticationFailedException extends AuthException {

  public AuthenticationFailedException() {
    super(AuthErrorCode.AUTHENTICATION_FAILED);
  }
}
