package com.otbooalone.module.location.service;

import com.otbooalone.module.location.api.KakaoLocaionApi;
import com.otbooalone.module.location.dto.data.LocationDto;
import com.otbooalone.module.location.dto.response.LocationApiResponse.Document;
import com.otbooalone.module.location.entity.Location;
import com.otbooalone.module.location.exception.LocationNotFoundException;
import com.otbooalone.module.location.mapper.LocationMapper;
import com.otbooalone.module.location.repository.LocationRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class LocationService {

  private final LocationRepository locationRepository;
  private final KakaoLocaionApi kakaoLocaionApi;

  private final LocationMapper locationMapper;

  public LocationDto create(double longitude, double latitude) {
    Document document = kakaoLocaionApi.findByApi(longitude, latitude);
    Location location = Location.create(latitude, longitude, document.x(), document.y(),
        document.address_name());
    return locationMapper.toDto(location);
  }

  @Transactional(readOnly = true)
  public LocationDto findById(UUID id) {

    Location location = locationRepository.findById(id)
        .orElseThrow(() -> LocationNotFoundException.withId(id));

    return locationMapper.toDto(location);
  }
}
