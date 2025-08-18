package com.otbooalone.global.config.initializer;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "admin")
public record AdminProperty(
    String name,
    String email,
    String password
) {

}
