package com.capstone.mall.service.review;

import com.capstone.mall.model.ResponseDto;
import com.capstone.mall.model.review.ReviewCreateRequestDto;
import com.capstone.mall.model.review.ReviewUpdateRequestDto;

public interface ReviewService {

    ResponseDto readReviewListByItemId(Long itemId, int pageNum, int pageSize, String sort, String sortType);

    ResponseDto createReview(String userId, ReviewCreateRequestDto reviewRequestDto);

    ResponseDto updateReview(Long reviewId, ReviewUpdateRequestDto reviewRequestDto, String token);

    ResponseDto deleteReview(Long reviewId, String token);

    ResponseDto readReviewListByUserId(String userId, int pageNum, int pageSize, String token);
}
