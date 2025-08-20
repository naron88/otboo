package com.otbooalone.module.storage;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorage {

  String create(MultipartFile file, StorageDomain domain);

  boolean delete(String url);

}
