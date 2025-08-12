package com.otbooalone.module.auth.controller;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.otbooalone.module.auth.entity.AuthCookieNames;
import com.otbooalone.module.auth.entity.GeneratedToken;
import com.otbooalone.module.auth.service.AuthService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WithMockUser
@WebMvcTest(AuthController.class)
class AuthControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private AuthService authService;

  @Test
  void testGetCsrfToken() throws Exception {
    mockMvc.perform(get("/api/auth/csrf-token"))
        .andExpect(status().isOk());
  }

  @Test
  void testGetAccessToken_Success() throws Exception {
    String refreshToken = "validRefreshToken";
    String expectedAccessToken = "access-token";

    when(authService.getAccessTokenByRefreshToken(refreshToken)).thenReturn(expectedAccessToken);

    mockMvc.perform(get("/api/auth/me")
            .cookie(new Cookie(AuthCookieNames.REFRESH_TOKEN_COOKIE_NAME, refreshToken)))
        .andExpect(status().isOk())
        .andExpect(content().string(expectedAccessToken));
  }

  @Test
  void testGetAccessToken_MissingRefreshToken() throws Exception {
    mockMvc.perform(get("/api/auth/me"))
        .andExpect(status().isUnauthorized()); // 인증 요구 예외일 경우 상태 코드 맞춰서 변경
  }

  @Test
  void testRefreshTokens_Success() throws Exception {
    String refreshToken = "validRefreshToken";
    String newAccessToken = "newAccessToken";
    String newRefreshToken = "newRefreshToken";

    GeneratedToken generatedToken = new GeneratedToken(newAccessToken, newRefreshToken);

    when(authService.refreshTokens(refreshToken)).thenReturn(generatedToken);

    mockMvc.perform(post("/api/auth/refresh")
            .cookie(new Cookie(AuthCookieNames.REFRESH_TOKEN_COOKIE_NAME, refreshToken))
            .with(csrf()))
        .andExpect(status().isOk())
        .andExpect(content().string(newAccessToken))
        .andExpect(cookie().value(AuthCookieNames.REFRESH_TOKEN_COOKIE_NAME, newRefreshToken));
  }

  @Test
  void testRefreshTokens_MissingRefreshToken() throws Exception {
    mockMvc.perform(post("/api/auth/refresh")
            .with(csrf()))
        .andExpect(status().isBadRequest());
  }
}