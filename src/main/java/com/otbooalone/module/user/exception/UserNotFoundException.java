package com.otbooalone.module.user.exception;

public class UserNotFoundException extends UserException {

  public UserNotFoundException() {
    super(UserErrorCode.USER_NOT_FOUND);
  }

  public static UserNotFoundException withEmail(String email) {
    UserNotFoundException exception = new UserNotFoundException();
    exception.addDetail("email", email);
    return exception;
  }

  public static UserNotFoundException withUsername(String username) {
    UserNotFoundException exception = new UserNotFoundException();
    exception.addDetail("username", username);
    return exception;
  }
}
