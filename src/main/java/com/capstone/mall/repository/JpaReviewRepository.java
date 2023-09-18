package com.capstone.mall.repository;

import com.capstone.mall.model.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByItemIdOrderByCreatedAtDesc(Long itemId);

    List<Review> findAllByUserId(String userId);

    List<Review> findAllByItemIdOrderByRateDesc(Long itemId);

    List<Review> findAllByItemIdOrderByRate(Long itemId);
}
