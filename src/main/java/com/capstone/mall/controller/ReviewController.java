package com.capstone.mall.controller;

import com.capstone.mall.model.ResponseDto;
import com.capstone.mall.model.review.ReviewRequestDto;
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

    @GetMapping("/public/reviews/{itemId}")
    public ResponseEntity<ResponseDto> readReviewList(@PathVariable Long itemId,
                                                      @RequestParam(required = false, defaultValue = "n") String sortType,
                                                      @RequestParam(required = false, defaultValue = "1") int pageNum,
                                                      @RequestParam(required = false, defaultValue = "10") int pageSize) {

        ResponseDto responseDto = reviewService.readReviewList(itemId, pageNum, pageSize, sortType);

        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @GetMapping("/users/reviews/{userId}")
    public ResponseEntity<ResponseDto> readReview(@PathVariable String userId,
                                                  @RequestParam(required = false, defaultValue = "1") int pageNum,
                                                  @RequestParam(required = false, defaultValue = "10") int pageSize,
                                                  @RequestHeader("Authorization") String token) {
        ResponseDto responseDto = reviewService.readReviewListByUserId(userId, pageNum, pageSize, token);

        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @PostMapping("/users/reviews/{userId}")
    public ResponseEntity<ResponseDto> createReview(@PathVariable String userId,
                                                    @RequestBody ReviewRequestDto reviewRequestDto) {
        ResponseDto responseDto = reviewService.createReview(userId, reviewRequestDto);

        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @PatchMapping("/users/reviews/{reviewId}")
    public ResponseEntity<ResponseDto> updateReview(@PathVariable Long reviewId, @RequestBody ReviewRequestDto reviewRequestDto, @RequestHeader("Authorization") String token) {
        ResponseDto responseDto = reviewService.updateReview(reviewId, reviewRequestDto, token);

        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @DeleteMapping("/users/reviews/{reviewId}")
    public ResponseEntity<ResponseDto> deleteReview(@PathVariable Long reviewId, @RequestHeader("Authorization") String token) {
        ResponseDto responseDto = reviewService.deleteReview(reviewId, token);

        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }
}
