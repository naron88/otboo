package com.otbooalone.module.user.exception;

import com.otbooalone.global.exception.BaseException;
import com.otbooalone.global.exception.ErrorCode;

public class UserException extends BaseException {

  public UserException(ErrorCode errorCode) {
    super(errorCode);
  }
}
