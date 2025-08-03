package com.otbooalone.global.config;

import com.otbooalone.global.infra.logging.P6spyPrettySqlFormatter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Configuration;

@Configuration
public class P6spyMeterRegistryConfig {

  public P6spyMeterRegistryConfig(MeterRegistry meterRegistry) {
    P6spyPrettySqlFormatter.setMeterRegistry(meterRegistry);
  }
}
