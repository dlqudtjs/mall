package com.capstone.mall.service.review;

import com.capstone.mall.model.ResponseDto;
import com.capstone.mall.model.review.Review;
import com.capstone.mall.model.review.ReviewRequestDto;
import com.capstone.mall.repository.JpaReviewRepository;
import com.capstone.mall.service.response.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private final ResponseService responseService;
    private final JpaReviewRepository reviewRepository;

    @Override
    public ResponseDto readReviewList(Long itemId, int pageNum, int pageSize, String sortType) {

        return null;
    }

    @Override
    public ResponseDto createReview(Long itemId, ReviewRequestDto reviewRequestDto) {
        Review review = Review.builder()
                .itemId(itemId)
                .userId(reviewRequestDto.getUserId())
                .content(reviewRequestDto.getContent())
                .image(reviewRequestDto.getImage())
                .rate(reviewRequestDto.getRate())
                .build();

        Review savedReview = reviewRepository.save(review);

        return responseService.createResponseDto(200, "", savedReview.getReviewId());
    }

    @Override
    public ResponseDto updateReview(Long reviewId, ReviewRequestDto reviewRequestDto) {
        Review review = reviewRepository.findById(reviewId).orElse(null);

        if (review == null) {
            return responseService.createResponseDto(200, "존재하지 않는 리뷰입니다.", null);
        }

        review.setUserId(reviewRequestDto.getUserId());
        review.setContent(reviewRequestDto.getContent());
        review.setImage(reviewRequestDto.getImage());
        review.setRate(reviewRequestDto.getRate());

        return responseService.createResponseDto(200, "", review.getReviewId());
    }

    @Override
    public ResponseDto deleteReview(Long reviewId) {
        return null;
    }
}
