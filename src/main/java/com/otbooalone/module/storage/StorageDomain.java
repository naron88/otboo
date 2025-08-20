package com.otbooalone.module.storage;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StorageDomain {

  PROFILE("profile");

  private final String folderName;
}
