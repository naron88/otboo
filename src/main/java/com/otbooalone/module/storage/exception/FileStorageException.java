package com.otbooalone.module.storage.exception;

import com.otbooalone.global.exception.BaseException;
import com.otbooalone.global.exception.ErrorCode;
import java.util.Map;

public class FileStorageException extends BaseException {


  public FileStorageException(ErrorCode errorCode) {
    super(errorCode);
  }

  public FileStorageException(String message, ErrorCode errorCode) {
    super(message, errorCode);
  }

  public FileStorageException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }
}
