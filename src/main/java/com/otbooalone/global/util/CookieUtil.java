package com.otbooalone.global.util;

import com.otbooalone.module.auth.entity.AuthCookieNames;
import jakarta.servlet.http.Cookie;

// 리프레시 토큰용 쿠키
public class CookieUtil {

  public static Cookie createRefreshTokenCookie(String token) {

    Cookie cookie = new Cookie(AuthCookieNames.REFRESH_TOKEN_COOKIE_NAME, token);
    cookie.setHttpOnly(true);
    cookie.setPath("/");
    return cookie;
  }

  public static Cookie expireRefreshTokenCookie() {

    Cookie cookie = new Cookie(AuthCookieNames.REFRESH_TOKEN_COOKIE_NAME, "");
    cookie.setHttpOnly(true);
    cookie.setPath("/");
    // 브라우저가 쿠키 즉시 삭제
    cookie.setMaxAge(0);
    return cookie;
  }
}
