package com.otbooalone.module.location.dto.data;

import java.util.List;
import java.util.UUID;

public record LocationDto(
    UUID id,
    double latitude,
    double longitude,
    double x,
    double y,
    List<String> locationNames
) {

}
