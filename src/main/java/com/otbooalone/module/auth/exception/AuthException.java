package com.otbooalone.module.auth.exception;

import com.otbooalone.global.exception.BaseException;
import com.otbooalone.global.exception.ErrorCode;

public class AuthException extends BaseException {

  public AuthException(ErrorCode errorCode) {
    super(errorCode);
  }
}
