package com.capstone.mall.controller;

import com.capstone.mall.model.ResponseDto;
import com.capstone.mall.model.review.ReviewCreateRequestDto;
import com.capstone.mall.model.review.ReviewUpdateRequestDto;
import com.capstone.mall.service.review.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {

    private final ReviewService reviewService;

    // 상품 리뷰 조회
    @PostMapping("/users/reviews/{userId}")
    public ResponseEntity<ResponseDto> createReview(@PathVariable String userId,
                                                    @RequestBody ReviewCreateRequestDto reviewRequestDto) {
        ResponseDto responseDto = reviewService.createReview(userId, reviewRequestDto);

        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }
    
    @GetMapping("/public/reviews/{itemId}")
    public ResponseEntity<ResponseDto> readReviewByItemId(@PathVariable Long itemId,
                                                          @RequestParam(required = false, defaultValue = "createdAt") String sort,
                                                          @RequestParam(required = false, defaultValue = "desc") String sortType,
                                                          @RequestParam(required = false, defaultValue = "0") int pageNum,
                                                          @RequestParam(required = false, defaultValue = "10") int pageSize) {

        ResponseDto responseDto = reviewService.readReviewListByItemId(itemId, pageNum, pageSize, sort, sortType);

        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    // 유저가 작성한 리뷰 조회
    @GetMapping("/users/reviews/{userId}")
    public ResponseEntity<ResponseDto> readReviewByUserId(@PathVariable String userId,
                                                          @RequestParam(required = false, defaultValue = "0") int pageNum,
                                                          @RequestParam(required = false, defaultValue = "10") int pageSize,
                                                          @RequestHeader("Authorization") String token) {
        ResponseDto responseDto = reviewService.readReviewListByUserId(userId, pageNum, pageSize, token);

        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }


    @PatchMapping("/users/reviews/{reviewId}")
    public ResponseEntity<ResponseDto> updateReview(@PathVariable Long reviewId,
                                                    @RequestBody ReviewUpdateRequestDto reviewRequestDto,
                                                    @RequestHeader("Authorization") String token) {
        ResponseDto responseDto = reviewService.updateReview(reviewId, reviewRequestDto, token);

        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @DeleteMapping("/users/reviews/{reviewId}")
    public ResponseEntity<ResponseDto> deleteReview(@PathVariable Long reviewId, @RequestHeader("Authorization") String token) {
        ResponseDto responseDto = reviewService.deleteReview(reviewId, token);

        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }
}
