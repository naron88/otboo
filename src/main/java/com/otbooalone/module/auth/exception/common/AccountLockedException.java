package com.otbooalone.module.auth.exception.common;

import com.otbooalone.module.auth.exception.AuthErrorCode;
import com.otbooalone.module.auth.exception.AuthException;

public class AccountLockedException extends AuthException {

  public AccountLockedException() {
    super(AuthErrorCode.ACCOUNT_LOCKED);
  }

  public static AccountLockedException noDetail() {
    return new AccountLockedException();
  }
}
