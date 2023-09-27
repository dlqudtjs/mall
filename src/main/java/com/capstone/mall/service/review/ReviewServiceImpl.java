package com.capstone.mall.service.review;

import com.capstone.mall.model.ResponseDto;
import com.capstone.mall.model.item.Item;
import com.capstone.mall.model.review.*;
import com.capstone.mall.repository.JpaItemRepository;
import com.capstone.mall.repository.JpaReviewRepository;
import com.capstone.mall.security.JwtTokenProvider;
import com.capstone.mall.service.response.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @Override
    public ResponseDto readReviewList(Long itemId, int pageNum, int pageSize, String sort, String sortType) {
        Sort pageSort = getSort(sort, sortType);

        Pageable pageable = getPageable(pageNum, pageSize, pageSort);
        Page<Review> reviews = reviewRepository.findAllByItemId(itemId, pageable);

        ReviewListResponseDto reviewListResponseDto = ReviewListResponseDto.builder()
                .reviews(reviews.getContent())
                .totalPage(reviews.getTotalPages())
                .build();

        return responseService.createResponseDto(200, "", reviewListResponseDto);
    }


    // 유저가 작성한 리뷰 조회
    @Override
    public ResponseDto readReviewListByUserId(String userId, int pageNum, int pageSize, String token) {
        // 토큰의 userId와 요청의 userId가 일치하지 않을 경우
        if (!userId.equals(jwtTokenProvider.getUserIdByBearerToken(token))) {
            return responseService.createResponseDto(403, "token does not match", null);
        }

        Pageable pageable = getPageable(pageNum, pageSize);
        Page<Review> reviews = reviewRepository.findAllByUserId(userId, pageable);

        List<ReviewListByUserId> reviewListByUserId = new ArrayList<>();
        for (Review review : reviews.getContent()) {
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
                .totalPage(reviews.getTotalPages())
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

    private Pageable getPageable(int pageNum, int pageSize) {
        pageNum = Math.max(pageNum, 0);
        pageSize = Math.max(pageSize, 1);

        return PageRequest.of(pageNum, pageSize);
    }

    private Pageable getPageable(int pageNum, int pageSize, Sort pageSort) {
        pageNum = Math.max(pageNum, 0);
        pageSize = Math.max(pageSize, 1);

        return PageRequest.of(pageNum, pageSize, pageSort);
    }

    private Sort getSort(String sort, String sortType) {
        // 정렬 기준이 rate, createdAt이 아닐 경우 default 로 createdAt 으로 정렬
        if (!sort.equals("rate") && !sort.equals("createdAt")) {
            sort = "createdAt";
        }

        if (sortType.equals("asc")) {
            return Sort.by(sort).descending();
        }

        return Sort.by(sort).ascending();
    }

    private List<Review> pagination(List<Review> reviewList, int pageNum, int pageSize) {
        pageNum = Math.max(pageNum, 1);
        int endIdx = Math.min(pageNum * pageSize, reviewList.size());

        reviewList = reviewList.subList((pageNum - 1) * pageSize, endIdx);

        return reviewList;
    }
}
