package com.capstone.mall.service.review;

import com.capstone.mall.model.ResponseDto;
import com.capstone.mall.model.review.Review;
import com.capstone.mall.model.review.ReviewRequestDto;
import com.capstone.mall.repository.JpaReviewRepository;
import com.capstone.mall.service.response.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private final ResponseService responseService;
    private final JpaReviewRepository reviewRepository;

    /*
     * sortType:
     * n: newest,
     * lr: low rate,
     * hr: high rate
     */
    @Override
    public ResponseDto readReviewList(Long itemId, int pageNum, int pageSize, String sortType) {
        List<Review> reviews = null;

        if (sortType.equals("n")) {
            reviews = reviewRepository.findAllByItemIdOrderByCreatedAtDesc(itemId);
        } else if (sortType.equals("lr")) {
            reviews = reviewRepository.findAllByItemIdOrderByRate(itemId);
        } else if (sortType.equals("hr")) {
            reviews = reviewRepository.findAllByItemIdOrderByRateDesc(itemId);
        } else {
            reviews = reviewRepository.findAllByItemIdOrderByCreatedAtDesc(itemId);
        }

        // 총 페이지 수
        int totalPage = (int) Math.ceil(reviews.size() / (double) pageSize);

        reviews = pagination(reviews, pageNum, pageSize);

        return responseService.createResponseDto(200, "", reviews);
    }

    private List<Review> pagination(List<Review> reviewList, int pageNum, int pageSize) {
        pageNum = Math.max(pageNum, 1);
        int endIdx = Math.min(pageNum * pageSize, reviewList.size());

        reviewList = reviewList.subList((pageNum - 1) * pageSize, endIdx);

        return reviewList;
    }

    @Override
    public ResponseDto createReview(ReviewRequestDto reviewRequestDto) {
        Review review = Review.builder()
                .itemId(reviewRequestDto.getItemId())
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
        if (!reviewRepository.existsById(reviewId)) {
            return responseService.createResponseDto(200, "review does not exist", null);
        }

        reviewRepository.deleteById(reviewId);

        return responseService.createResponseDto(200, "", reviewId);
    }
}
