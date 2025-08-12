package com.otbooalone.global.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.otbooalone.module.auth.filter.JsonLoginAuthenticationFilter;
import com.otbooalone.module.auth.filter.jwt.JwtAuthenticationFilter;
import com.otbooalone.module.auth.handler.CustomAccessDeniedHandler;
import com.otbooalone.module.auth.handler.CustomAuthenticationEntryPoint;
import com.otbooalone.module.auth.handler.CustomLogoutHandler;
import com.otbooalone.module.auth.handler.CustomLogoutSuccessHandler;
import com.otbooalone.module.auth.handler.JsonLoginFailureHandler;
import com.otbooalone.module.auth.handler.JsonLoginSuccessHandler;
import com.otbooalone.module.auth.provider.CustomDaoAuthenticationProvider;
import com.otbooalone.module.auth.provider.JwtTokenProvider;
import com.otbooalone.module.auth.service.CustomUserDetailsService;
import com.otbooalone.module.user.entity.User.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
// @PreAuthorize, @PostAuthorize 사용을 위해
@EnableMethodSecurity
@EnableWebSecurity
@RequiredArgsConstructor
@EnableConfigurationProperties(JwtProperty.class)
public class SecurityConfig {

  // jwt
  private final JwtTokenProvider jwtTokenProvider;

  // 인증, 인가
  private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
  private final CustomAccessDeniedHandler customAccessDeniedHandler;

  // 로그아웃
  private final CustomLogoutHandler customLogoutHandler;
  private final CustomLogoutSuccessHandler customLogoutSuccessHandler;

  // 사용자 관련
  private final CustomUserDetailsService customUserDetailsService;
  private final PasswordEncoder passwordEncoder;

  @Bean
  public SecurityFilterChain filterChain(
      HttpSecurity http,
      JsonLoginAuthenticationFilter jsonLoginAuthenticationFilter,
      AuthenticationProvider customDaoAuthenticationProvider
  ) throws Exception {
    return http

        .cors(AbstractHttpConfigurer::disable)
        .csrf(AbstractHttpConfigurer::disable)

        .authenticationProvider(customDaoAuthenticationProvider)

        // 인가 설정
        .authorizeHttpRequests(this::configureAuthorization)

        // 로그아웃
        .logout(logout -> logout
            .logoutUrl("/api/auth/sign-out")
            .addLogoutHandler(customLogoutHandler)
            .logoutSuccessHandler(customLogoutSuccessHandler)
        )

        // 예외 핸들러(401, 403)
        .exceptionHandling(ex -> ex
            .authenticationEntryPoint(customAuthenticationEntryPoint)
            .accessDeniedHandler(customAccessDeniedHandler)
        )

        // jwt 필터 추가
        .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
        .addFilterAt(jsonLoginAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

        .build();
  }

  // 인가 설정
  private void configureAuthorization(
      AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth
  ) {
    auth
        // 회원가입
        .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
        // 인증 및 인가
        .requestMatchers("/api/auth/**").permitAll()
        // api 문서
        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
        // 이미지
        .requestMatchers("/file/**").permitAll()
        // sse
        .requestMatchers("/api/sse").permitAll()

        .requestMatchers("/api/**").hasRole(Role.USER.name())

        .anyRequest().permitAll();
  }

  // 인증 처리
  @Bean
  public AuthenticationManager authenticationManager(
      AuthenticationConfiguration authenticationConfiguration) throws Exception {

    return authenticationConfiguration.getAuthenticationManager();
  }

  // 인증 커스텀
  @Bean
  public AuthenticationProvider customDaoAuthenticationProvider() {
    return new CustomDaoAuthenticationProvider(
        customUserDetailsService,
        passwordEncoder
    );
  }

  // json 로그인 필터 등록
  @Bean
  public JsonLoginAuthenticationFilter jsonLoginAuthenticationFilter(
      AuthenticationManager authManager,
      ObjectMapper objectMapper,
      JsonLoginSuccessHandler successHandler,
      JsonLoginFailureHandler failureHandler
  ) {
    JsonLoginAuthenticationFilter filter = new JsonLoginAuthenticationFilter(authManager,
        objectMapper);
    filter.setAuthenticationSuccessHandler(successHandler);
    filter.setAuthenticationFailureHandler(failureHandler);
    return filter;
  }

  // jwt 필터 등록
  @Bean
  public JwtAuthenticationFilter jwtAuthenticationFilter() {
    return new JwtAuthenticationFilter(jwtTokenProvider, customAuthenticationEntryPoint);
  }

  // 계층 설정
  @Bean
  public RoleHierarchy roleHierarchy() {
    return RoleHierarchyImpl.fromHierarchy("""
      ROLE_ADMIN > ROLE_USER
      """);
  }

  // 메서드 보안 설정
  @Bean
  public MethodSecurityExpressionHandler methodSecurityExpressionHandler(
      RoleHierarchy roleHierarchy) {
    DefaultMethodSecurityExpressionHandler handler = new DefaultMethodSecurityExpressionHandler();
    handler.setRoleHierarchy(roleHierarchy);
    return handler;
  }
}
