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

    @PostMapping("/users/reviews")
    public ResponseEntity<ResponseDto> createReview(@RequestBody ReviewRequestDto reviewRequestDto) {
        ResponseDto responseDto = reviewService.createReview(reviewRequestDto);

        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @PatchMapping("/public/users/reviews/{reviewId}")
    public ResponseEntity<ResponseDto> updateReview(@PathVariable Long reviewId, @RequestBody ReviewRequestDto reviewRequestDto) {
        ResponseDto responseDto = reviewService.updateReview(reviewId, reviewRequestDto);

        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @DeleteMapping("/public/users/reviews/{reviewId}")
    public ResponseEntity<ResponseDto> deleteReview(@PathVariable Long reviewId) {
        ResponseDto responseDto = reviewService.deleteReview(reviewId);

        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }
}
