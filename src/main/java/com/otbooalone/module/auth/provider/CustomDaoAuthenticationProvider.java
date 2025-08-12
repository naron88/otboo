package com.otbooalone.module.auth.provider;

import com.otbooalone.module.auth.service.CustomUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.password.PasswordEncoder;

// DB에서 사용자 정보를 가져옴
@Slf4j
public class CustomDaoAuthenticationProvider extends DaoAuthenticationProvider {

  public CustomDaoAuthenticationProvider(CustomUserDetailsService userDetailsService,
      PasswordEncoder passwordEncoder) {
    super(userDetailsService);
    setPasswordEncoder(passwordEncoder);
  }
}

