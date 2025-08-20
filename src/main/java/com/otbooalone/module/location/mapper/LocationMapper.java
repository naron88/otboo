package com.otbooalone.module.location.mapper;

import com.otbooalone.module.location.dto.data.LocationDto;
import com.otbooalone.module.location.entity.Location;
import java.util.Arrays;
import org.springframework.stereotype.Component;

@Component
public class LocationMapper {

  public LocationDto toDto(Location location) {
    return new LocationDto(
        location.getId(),
        location.getLatitude(),
        location.getLongitude(),
        location.getX(),
        location.getY(),
        Arrays.asList(location.getName().split(" "))
    );
  }
}
