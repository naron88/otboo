package com.otbooalone.module.auth.exception.jwt;

public class InvalidJwtSignatureException extends JwtAuthenticationException {

  public InvalidJwtSignatureException(String msg) {
    super(msg);
  }
}

