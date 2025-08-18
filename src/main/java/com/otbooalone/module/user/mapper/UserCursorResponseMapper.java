package com.otbooalone.module.user.mapper;

import com.otbooalone.global.enums.SortDirection;
import com.otbooalone.module.user.dto.data.UserDto;
import com.otbooalone.module.user.dto.data.UserDtoCursorResponse;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class UserCursorResponseMapper {

  public UserDtoCursorResponse toDto(
      List<UserDto> data,
      String nextCursor,
      UUID nextIdAfter,
      boolean hasNext,
      int totalCount,
      String sortBy,
      SortDirection sortDirection) {

    return new UserDtoCursorResponse(data, nextCursor, nextIdAfter, hasNext, totalCount, sortBy,
    sortDirection);
  }

}
