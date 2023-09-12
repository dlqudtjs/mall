package com.capstone.mall.repository;

import com.capstone.mall.model.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaReviewRepository extends JpaRepository<Review, Long> {
}
