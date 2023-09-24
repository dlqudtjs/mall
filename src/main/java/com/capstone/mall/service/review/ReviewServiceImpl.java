package com.capstone.mall.service.review;

import com.capstone.mall.model.ResponseDto;
import com.capstone.mall.model.item.Item;
import com.capstone.mall.model.review.*;
import com.capstone.mall.repository.JpaItemRepository;
import com.capstone.mall.repository.JpaReviewRepository;
import com.capstone.mall.security.JwtTokenProvider;
import com.capstone.mall.service.response.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private final ResponseService responseService;
    private final JpaReviewRepository reviewRepository;
    private final JpaItemRepository itemRepository;
    private final JwtTokenProvider jwtTokenProvider;

    /*
     * sortType:
     * n: newest,
     * lr: low rate,
     * hr: high rate
     */
    @Override
    public ResponseDto readReviewList(Long itemId, int pageNum, int pageSize, String sortType) {
        List<Review> reviews;

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

        // 페이지네이션
        reviews = pagination(reviews, pageNum, pageSize);

        ReviewListResponseDto reviewListResponseDto = ReviewListResponseDto.builder()
                .reviews(reviews)
                .totalPage(totalPage)
                .build();

        return responseService.createResponseDto(200, "", reviewListResponseDto);
    }

    @Override
    public ResponseDto readReviewListByUserId(String userId, int pageNum, int pageSize, String token) {
        List<Review> reviews = reviewRepository.findAllByUserId(userId);

        if (reviews == null) {
            return responseService.createResponseDto(200, "", null);
        }

        if (!userId.equals(jwtTokenProvider.getUserIdByBearerToken(token))) {
            return responseService.createResponseDto(403, "token does not match", null);
        }

        // 총 페이지 수
        int totalPage = (int) Math.ceil(reviews.size() / (double) pageSize);

        // 페이지네이션
        reviews = pagination(reviews, pageNum, pageSize);

        List<ReviewListByUserId> reviewListByUserId = new ArrayList<>();

        for (Review review : reviews) {
            Optional<Item> item = itemRepository.findById(review.getItemId());
            reviewListByUserId.add(ReviewListByUserId.builder()
                    .reviewId(review.getReviewId())
                    .userId(review.getUserId())
                    .itemId(review.getItemId())
                    .itemName(item.isEmpty() ? "삭제된 상품" : item.get().getName())
                    .itemImage(item.isEmpty() ? "" : item.get().getImage1())
                    .content(review.getContent())
                    .reviewImage(review.getImage())
                    .rate(review.getRate())
                    .createdAt(review.getCreatedAt())
                    .updatedAt(review.getUpdatedAt())
                    .build());
        }

        ReviewListByUserIdResponseDto reviewListByUserIdResponseDto = ReviewListByUserIdResponseDto.builder()
                .reviews(reviewListByUserId)
                .totalPage(totalPage)
                .build();


        return responseService.createResponseDto(200, "", reviewListByUserIdResponseDto);
    }


    @Override
    public ResponseDto createReview(String userId, ReviewRequestDto reviewRequestDto) {
        Review review = Review.builder()
                .itemId(reviewRequestDto.getItemId())
                .userId(userId)
                .content(reviewRequestDto.getContent())
                .image(reviewRequestDto.getImage())
                .rate(reviewRequestDto.getRate())
                .build();

        Review savedReview = reviewRepository.save(review);

        return responseService.createResponseDto(201, "", savedReview.getReviewId());
    }

    @Override
    public ResponseDto updateReview(Long reviewId, ReviewRequestDto reviewRequestDto, String token) {
        Review review = reviewRepository.findById(reviewId).orElse(null);

        if (review == null) {
            return responseService.createResponseDto(200, "review does not exist", null);
        }

        if (!review.getUserId().equals(jwtTokenProvider.getUserIdByBearerToken(token))) {
            return responseService.createResponseDto(403, "token does not match", null);
        }

        review.setUserId(reviewRequestDto.getUserId());
        review.setContent(reviewRequestDto.getContent());
        review.setImage(reviewRequestDto.getImage());
        review.setRate(reviewRequestDto.getRate());

        return responseService.createResponseDto(200, "", review.getReviewId());
    }

    @Override
    public ResponseDto deleteReview(Long reviewId, String token) {
        Review review = reviewRepository.findById(reviewId).orElse(null);

        if (review == null) {
            return responseService.createResponseDto(200, "review does not exist", null);
        }

        if (!review.getUserId().equals(jwtTokenProvider.getUserIdByBearerToken(token))) {
            return responseService.createResponseDto(403, "token does not match", null);
        }

        reviewRepository.deleteById(reviewId);

        return responseService.createResponseDto(200, "", reviewId);
    }

    private List<Review> pagination(List<Review> reviewList, int pageNum, int pageSize) {
        pageNum = Math.max(pageNum, 1);
        int endIdx = Math.min(pageNum * pageSize, reviewList.size());

        reviewList = reviewList.subList((pageNum - 1) * pageSize, endIdx);

        return reviewList;
    }

}
