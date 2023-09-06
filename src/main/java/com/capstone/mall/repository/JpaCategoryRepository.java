package com.capstone.mall.repository;

import com.capstone.mall.model.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaCategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByParentCategoryId(Long parentCategoryId);

    Optional<Category> findByCategoryId(Long categoryId);
}
