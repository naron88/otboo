package com.otbooalone.module.auth.dto.data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public record TempPasswordMetadata(
  boolean isUsed,
  LocalDateTime tempPasswordExpiresAt
) {

  public static TempPasswordMetadata notUsed() {
    return new TempPasswordMetadata(false, null);
  }

  public Instant expiresAtAsInstant() {
    if (this.tempPasswordExpiresAt == null) {
      return null;
    }
    return this.tempPasswordExpiresAt.atZone(ZoneId.systemDefault()).toInstant();
  }
}
