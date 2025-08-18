package com.otbooalone.global.config.initializer;

import com.otbooalone.module.user.dto.request.UserCreateRequest;
import com.otbooalone.module.user.entity.User.Role;
import com.otbooalone.module.user.exception.EmailAlreadyExistsException;
import com.otbooalone.module.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile("!test") // 테스트에서는 생성 X
@RequiredArgsConstructor
@EnableConfigurationProperties(AdminProperty.class)
public class AdminInitializer implements ApplicationRunner {

  private final UserService userService;
  private final AdminProperty adminProperty;

  @Override
  public void run(ApplicationArguments args) throws Exception {
    UserCreateRequest request = new UserCreateRequest(adminProperty.name(), adminProperty.email(),
        adminProperty.password());

    try {
      userService.create(request, Role.ADMIN);
      log.info("관리자 계정 생성 완료 (email: {})", adminProperty.email());
    } catch (EmailAlreadyExistsException e) {
      log.info("관리자 계정이 이미 존재합니다. (email: {})", adminProperty.email());
    }
  }
}
