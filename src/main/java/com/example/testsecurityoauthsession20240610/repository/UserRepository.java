package com.example.testsecurityoauthsession20240610.repository;

import com.example.testsecurityoauthsession20240610.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByUsername(String username);
}
