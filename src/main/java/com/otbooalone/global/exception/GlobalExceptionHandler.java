package com.otbooalone.global.exception;

import com.otbooalone.global.util.CookieUtil;
import com.otbooalone.module.auth.dto.security.CustomUserDetails;
import com.otbooalone.module.auth.exception.AuthErrorCode;
import com.otbooalone.module.auth.exception.AuthException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  // validation 처리
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex) {

    ErrorCode errorCode = CommonErrorCode.INVALID_INPUT_VALUE;
    Map<String, Object> errors = ex.getFieldErrors().stream()
        .collect(Collectors.toMap(
            FieldError::getField,
            FieldError::getRejectedValue,
            (existing, replacement) -> replacement // 중복 필드 있을 경우 마지막 값 사용
        ));

    log.warn("Validation failed for fields: {}", errors.keySet());

    ErrorResponse errorResponse = ErrorResponse.of(
        ex.getClass().getSimpleName(),
        errorCode.getMessage(),
        errors
    );

    return createErrorResponseEntity(errorCode.getHttpStatus(), errorResponse);
  }

  // request method
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupport(
      HttpRequestMethodNotSupportedException ex) {

    log.info("Request method not supported: {}", ex.getMethod());

    CommonErrorCode errorCode = CommonErrorCode.METHOD_NOT_ALLOWED;

    ErrorResponse errorResponse = ErrorResponse.of(
        ex.getClass().getSimpleName(),
        errorCode.getMessage()
    );

    return createErrorResponseEntity(errorCode.getHttpStatus(), errorResponse);
  }

  // resource : 잘못된 uri
  @ExceptionHandler(NoResourceFoundException.class)
  protected ResponseEntity<ErrorResponse> handleNoResourceFoundException(
      NoResourceFoundException ex) {

    log.info("Request for unsupported URI: {}", ex.getResourcePath());

    CommonErrorCode errorCode = CommonErrorCode.URI_NOT_FOUND;

    ErrorResponse errorResponse = ErrorResponse.of(
        ex.getClass().getSimpleName(),
        errorCode.getMessage()
    );

    return createErrorResponseEntity(errorCode.getHttpStatus(), errorResponse);
  }

  // 잘못된 입력 값 : 타입 불일치 등의 json 파싱 실패
  @ExceptionHandler(HttpMessageNotReadableException.class)
  protected ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
      HttpMessageNotReadableException ex) {

    log.warn("Invalid JSON format. Error: {}", ex.getMostSpecificCause().getMessage());

    CommonErrorCode errorCode = CommonErrorCode.INVALID_JSON_FORMAT;

    ErrorResponse errorResponse = ErrorResponse.of(
        ex.getClass().getSimpleName(),
        errorCode.getMessage()
    );

    return createErrorResponseEntity(errorCode.getHttpStatus(), errorResponse);
  }

  // method level security
  @ExceptionHandler(AuthorizationDeniedException.class)
  protected ResponseEntity<ErrorResponse> handleAuthorizationDeniedException(
      AuthorizationDeniedException ex,
      HttpServletRequest request,
      @AuthenticationPrincipal CustomUserDetails principal
  ) {
    log.warn("Authorization denied: {}, path: {}, userId: {}, role: {}",
        ex.getMessage(),
        request.getRequestURI(),
        principal != null ? principal.getId() : "anonymous",
        principal != null ? principal.getAuthorities() : "anonymous"
    );

    AuthErrorCode errorCode = AuthErrorCode.ACCESS_DENIED;

    ErrorResponse errorResponse = ErrorResponse.of(
        ex.getClass().getSimpleName(),
        errorCode.getMessage()
    );

    return createErrorResponseEntity(errorCode.getHttpStatus(), errorResponse);
  }

  // auth
  @ExceptionHandler(AuthException.class)
  protected ResponseEntity<ErrorResponse> handleAuthException(AuthException ex,
      HttpServletResponse response) {

    ErrorCode errorCode = ex.getErrorCode();

    log.info("Authentication failed : {} | Error: {}",
        errorCode, errorCode.getMessage());

    ErrorResponse errorResponse = ErrorResponse.of(
        AuthenticationException.class.getSimpleName(),
        errorCode.getMessage()
    );

    // refresh token 쿠키 무효화
    Cookie cookie = CookieUtil.expireRefreshTokenCookie();
    response.addCookie(cookie);

    return createErrorResponseEntity(errorCode.getHttpStatus(), errorResponse);
  }

  // business
  @ExceptionHandler(BaseException.class)
  protected ResponseEntity<ErrorResponse> handleBusinessException(BaseException ex) {

    ErrorCode errorCode = ex.getErrorCode();

    ErrorResponse errorResponse = ErrorResponse.of(
        ex.getClass().getSimpleName(),
        ex.getMessage(),
        ex.getDetails()
    );

    return createErrorResponseEntity(errorCode.getHttpStatus(), errorResponse);
  }

  // other
  @ExceptionHandler(Exception.class)
  protected ResponseEntity<ErrorResponse> handleUnexpectedException(Exception ex) {

    log.error("Exception", ex);

    ErrorCode errorCode = CommonErrorCode.INTERNAL_SERVER_ERROR;

    ErrorResponse errorResponse = ErrorResponse.of(
        ex.getClass().getSimpleName(),
        errorCode.getMessage()
    );

    return createErrorResponseEntity(errorCode.getHttpStatus(), errorResponse);
  }

  private ResponseEntity<ErrorResponse> createErrorResponseEntity(HttpStatus status,
      ErrorResponse errorResponse) {
    return ResponseEntity
        .status(status)
        .contentType(MediaType.APPLICATION_JSON)
        .body(errorResponse);
  }
}
