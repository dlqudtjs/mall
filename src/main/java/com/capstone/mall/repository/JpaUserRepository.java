package com.capstone.mall.repository;

import com.capstone.mall.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface JpaUserRepository extends JpaRepository<User, String> {

    Optional<User> findByUserId(String userId);

    List<User> findAll();

    @Query(value = "SELECT * FROM user WHERE meta_id = :metaId", nativeQuery = true)
    Optional<User> findByMetaIdNative(String metaId);
}
