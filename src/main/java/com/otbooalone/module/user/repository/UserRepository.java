package com.otbooalone.module.user.repository;

import com.otbooalone.module.user.entity.User;
import com.otbooalone.module.user.repository.custom.CustomUserRepository;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UUID>, CustomUserRepository {

  Optional<User> findByEmail(String email);

  boolean existsByEmail(String email);

}
