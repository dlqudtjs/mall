package com.capstone.mall.repository;


import com.capstone.mall.model.token.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JpaTokenRepository extends JpaRepository<Token, Long> {

    @Query(value = "CALL UpsertToken(:user_id, :refresh_token)", nativeQuery = true)
    void upsertToken(String user_id, String refresh_token);
}
