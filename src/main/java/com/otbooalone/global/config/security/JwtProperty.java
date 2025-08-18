package com.otbooalone.global.config.security;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "jwt")
public record JwtProperty(
    @NotBlank(message = "JWT 발행자는 필수입니다.")
    String issuer,

    @NotBlank(message = "JWT 시크릿 키는 필수입니다.")
    @Size(min = 32, message = "JWT 시크릿은 최소 32자 이상이어야 합니다.")
    String secret,

    @Valid TokenConfig accessToken,
    @Valid TokenConfig refreshToken
) {

  public record TokenConfig(
      @Min(value = 60, message = "토큰 유효시간은 최소 60초이어야 합니다.")
      long validitySeconds
  ) {}
}
