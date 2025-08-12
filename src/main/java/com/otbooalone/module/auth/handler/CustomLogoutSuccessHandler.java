package com.otbooalone.module.auth.handler;

import org.springframework.http.HttpStatus;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomLogoutSuccessHandler extends HttpStatusReturningLogoutSuccessHandler {

  public CustomLogoutSuccessHandler() {
    super(HttpStatus.NO_CONTENT);
  }
}
