package com.otbooalone.module.user.repository.custom.impl;

import com.otbooalone.global.enums.SortDirection;
import com.otbooalone.module.user.entity.QUser;
import com.otbooalone.module.user.entity.User;
import com.otbooalone.module.user.repository.custom.CustomUserRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomUserRepositoryImpl implements CustomUserRepository {

  private final JPAQueryFactory queryFactory;
  private final QUser user = QUser.user;

  @Override
  public List<User> findByKeyword(String emailLike) {

    return queryFactory
        .select(user)
        .from(user)
        .where(user.email.containsIgnoreCase(emailLike))
        .fetch();
  }

  @Override
  public List<User> findByCursor(List<UUID> userIdList, String cursor, UUID idAfter, int limit,
      String sortBy, SortDirection sortDirection) {

    BooleanBuilder where = new BooleanBuilder();

    where.and(user.id.in(userIdList));

    if (cursor != null && idAfter != null) {
      if (sortBy.equals("email")) {
        if (sortDirection == SortDirection.ASCENDING) {
          // 오름차순일 경우 큰 값
          where.and(
              user.email.gt(cursor)
                  .or(user.email.eq(cursor).and(user.id.gt(idAfter)))
          );
        } else {
          // 내림차순일 경우 작은 값
          where.and(
              user.email.lt(cursor)
                  .or(user.email.eq(cursor).and(user.id.lt(idAfter)))
          );
        }
      } else {
        LocalDateTime createdAt = LocalDateTime.parse(cursor);

        if (sortDirection == SortDirection.ASCENDING) {
          where.and(
              user.createdAt.gt(createdAt)
                  .or(user.createdAt.eq(createdAt).and(user.id.gt(idAfter)))
          );
        } else {
          where.and(
              user.createdAt.lt(createdAt)
                  .or(user.createdAt.eq(createdAt).and(user.id.lt(idAfter)))
          );
        }
      }
    }

    OrderSpecifier<?> order = getOrderSpecifier(sortBy, sortDirection);

    return queryFactory
        .selectFrom(user)
        .where(where)
        .orderBy(order)
        .limit(limit + 1)
        .fetch();
  }

  private OrderSpecifier<?> getOrderSpecifier(String sortBy, SortDirection sortDirection) {
    if (sortBy.equals("email")) {
      return sortDirection.equals(SortDirection.ASCENDING)
          ? user.email.asc()
          : user.email.desc();
    } else {
      return sortDirection.equals(SortDirection.ASCENDING)
          ? user.createdAt.asc()
          : user.createdAt.desc();
    }
  }
}
