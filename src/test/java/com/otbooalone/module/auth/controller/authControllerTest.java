package com.otbooalone.module.auth.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.otbooalone.module.auth.dto.request.UserCreateRequest;
import com.otbooalone.module.auth.service.AuthService;
import com.otbooalone.module.user.dto.data.UserDto;
import com.otbooalone.module.user.entity.User.Role;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WithMockUser
@WebMvcTest(authController.class)
class authControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private AuthService authService;

  @Nested
  @DisplayName("회원 가입")
  class Create {

    @Test
    @DisplayName("회원 가입 성공")
    void create_success() throws Exception {

      // given
      UserCreateRequest request = new UserCreateRequest("test", "test@test.com", "qwer1234!");

      UserDto response = new UserDto(UUID.randomUUID(), LocalDateTime.now(), request.email(),
          request.name(), Role.USER, List.of(), false);

      given(authService.create(request, Role.USER)).willReturn(response);

      // when, then
      mockMvc.perform(post("/api/users")
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(request))
              .accept(MediaType.APPLICATION_JSON)
              .with(csrf()))
          .andExpect(status().isCreated());
    }
  }
}