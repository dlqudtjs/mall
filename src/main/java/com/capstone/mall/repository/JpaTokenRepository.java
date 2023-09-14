package com.capstone.mall.repository;


import com.capstone.mall.model.token.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaTokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByUserId(String userId);
}
