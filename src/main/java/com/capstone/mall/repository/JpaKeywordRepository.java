package com.capstone.mall.repository;

import com.capstone.mall.model.keyword.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaKeywordRepository extends JpaRepository<Keyword, Long> {

    boolean existsByKeyword(String keyword);

    Keyword findByKeyword(String keyword);
}
