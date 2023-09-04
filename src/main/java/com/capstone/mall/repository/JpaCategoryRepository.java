package com.capstone.mall.repository;

import com.capstone.mall.model.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaCategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findAll();
}
