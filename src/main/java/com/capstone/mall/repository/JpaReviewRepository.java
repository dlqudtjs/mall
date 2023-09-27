package com.capstone.mall.repository;

import com.capstone.mall.model.review.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaReviewRepository extends JpaRepository<Review, Long> {

    Page<Review> findAllByUserId(String userId, Pageable pageable);

    Page<Review> findAllByItemId(Long itemId, Pageable pageable);
}
