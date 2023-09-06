package com.capstone.mall.repository;

import com.capstone.mall.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaUserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserId(String userId);
}
