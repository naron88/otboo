package com.otbooalone.module.storage.exception;

public class FileUploadFailedException extends FileStorageException {

  public FileUploadFailedException() {
    super(FileStorageErrorCode.FILE_UPLOAD_FAIL);
  }

  public static FileUploadFailedException withFileName(String fileName) {
    FileUploadFailedException exception = new FileUploadFailedException();
    exception.addDetail("fileName", fileName);
    return exception;
  }
}
