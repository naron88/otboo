package com.otbooalone.module.user.dto.data;

import com.otbooalone.global.enums.SortDirection;
import java.util.List;
import java.util.UUID;

public record UserDtoCursorResponse(

        List<UserDto> data,
        String nextCursor,
        UUID nextIdAfter,
        boolean hasNext,
        int totalCount,
        String sortBy,
        SortDirection sortDirection
) {
}
