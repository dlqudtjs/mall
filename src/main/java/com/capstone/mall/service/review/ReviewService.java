package com.capstone.mall.service.review;

import com.capstone.mall.model.ResponseDto;
import com.capstone.mall.model.review.ReviewRequestDto;

public interface ReviewService {

    ResponseDto readReviewList(Long itemId, int pageNum, int pageSize, String sortType);

    ResponseDto createReview(String userId, ReviewRequestDto reviewRequestDto);

    ResponseDto updateReview(Long reviewId, ReviewRequestDto reviewRequestDto);

    ResponseDto deleteReview(Long reviewId);

    ResponseDto readReviewListByUserId(String userId, int pageNum, int pageSize);
}
