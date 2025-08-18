package com.otbooalone.module.user.repository.custom;

import com.otbooalone.global.enums.SortDirection;
import com.otbooalone.module.user.entity.User;
import java.util.List;
import java.util.UUID;

public interface CustomUserRepository {

  List<User> findByKeyword(String emailLike);

  List<User> findByCursor(List<UUID> list, String cursor, UUID idAfter, int limit, String sortBy, SortDirection sortDirection);
}
