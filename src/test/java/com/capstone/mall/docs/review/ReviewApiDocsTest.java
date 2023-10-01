package com.capstone.mall.docs.review;

import com.capstone.mall.controller.ReviewController;
import com.capstone.mall.docs.RestDocumentSupport;
import com.capstone.mall.model.ResponseDto;
import com.capstone.mall.model.review.*;
import com.capstone.mall.service.review.ReviewService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ReviewController.class)
public class ReviewApiDocsTest extends RestDocumentSupport {

    @MockBean
    private ReviewService reviewService;

    @Test
    void createReview() throws Exception {
        // request
        ReviewCreateRequestDto reviewCreateRequestDto = ReviewCreateRequestDto.builder()
                .itemId(1L)
                .content("testContent")
                .image("testImage")
                .rate(5)
                .build();

        // given
        given(reviewService.createReview(any(), any())).willReturn(
                ResponseDto.builder()
                        .code(HttpStatus.OK.value())
                        .message("")
                        .data(1L)
                        .build());

        // when & then
        mockMvc.perform(post("/api/users/reviews/{userId}", "testId")
                        .header("Authorization", "Bearer " + "token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewCreateRequestDto)))
                .andExpect(status().isOk())
                .andDo(document("{class-name}/{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("JWT 토큰")
                        ),
                        requestFields(
                                fieldWithPath("itemId").description("상품 ID"),
                                fieldWithPath("content").description("리뷰 내용"),
                                fieldWithPath("image").description("리뷰 이미지"),
                                fieldWithPath("rate").description("리뷰 평점")
                        ),
                        responseFields(
                                fieldWithPath("code").description("상태코드"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("data").description("작성한 리뷰 ID")
                        )
                ));
    }

    @Test
    void readReviewByItemId() throws Exception {
        // response List
        List<Review> reviews = List.of(
                Review.builder()
                        .reviewId(1L)
                        .userId("testId")
                        .itemId(1L)
                        .content("testContent")
                        .image("testImage")
                        .createdAt(new Date())
                        .updatedAt(new Date())
                        .rate(5)
                        .build(),
                Review.builder()
                        .reviewId(2L)
                        .userId("testId2")
                        .itemId(2L)
                        .content("testContent2")
                        .image("testImage2")
                        .createdAt(new Date())
                        .updatedAt(new Date())
                        .rate(4)
                        .build()
        );

        // given
        ReviewListResponseDto reviewListResponseDto = ReviewListResponseDto.builder()
                .reviews(reviews)
                .totalPage(1)
                .build();

        given(reviewService.readReviewListByItemId(any(), any(Integer.class), any(Integer.class), any(), any())).willReturn(
                ResponseDto.builder()
                        .code(HttpStatus.OK.value())
                        .message("")
                        .data(reviewListResponseDto)
                        .build());

        // when & then
        mockMvc.perform(get("/api/public/reviews/{itemId}", 1L)
                        .param("sort", "createdAt")
                        .param("sortType", "desc")
                        .param("pageNum", "0")
                        .param("pageSize", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("{class-name}/{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("sort").description("정렬 기준 (default: createdAt)"),
                                parameterWithName("sortType").description("정렬 타입 (default: desc)"),
                                parameterWithName("pageNum").description("페이지 번호 (default: 0)"),
                                parameterWithName("pageSize").description("페이지 사이즈 (default: 10)")
                        ),
                        responseFields(
                                fieldWithPath("code").description("상태 코드"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("data.reviews[].reviewId").description("리뷰 ID"),
                                fieldWithPath("data.reviews[].userId").description("유저 ID"),
                                fieldWithPath("data.reviews[].itemId").description("상품 ID"),
                                fieldWithPath("data.reviews[].content").description("리뷰 내용"),
                                fieldWithPath("data.reviews[].image").description("리뷰 이미지"),
                                fieldWithPath("data.reviews[].createdAt").description("리뷰 생성일"),
                                fieldWithPath("data.reviews[].updatedAt").description("리뷰 수정일"),
                                fieldWithPath("data.reviews[].rate").description("리뷰 평점"),
                                fieldWithPath("data.totalPage").description("총 페이지 수")
                        )
                ));
    }

    @Test
    void readReviewByUserId() throws Exception {
        // response List
        List<ReviewListByUserId> reviews = List.of(
                ReviewListByUserId.builder()
                        .reviewId(1L)
                        .userId("testId")
                        .itemId(1L)
                        .itemName("testItemName")
                        .itemImage("testItemImage")
                        .content("testContent")
                        .reviewImage("testReviewImage")
                        .rate(5)
                        .createdAt(new Date())
                        .updatedAt(new Date())
                        .build(),
                ReviewListByUserId.builder()
                        .reviewId(2L)
                        .userId("testId2")
                        .itemId(2L)
                        .itemName("testItemName2")
                        .itemImage("testItemImage2")
                        .content("testContent2")
                        .reviewImage("testReviewImage2")
                        .rate(4)
                        .createdAt(new Date())
                        .updatedAt(new Date())
                        .build()
        );

        // given
        given(reviewService.readReviewListByUserId(any(), any(Integer.class), any(Integer.class), any())).willReturn(
                ResponseDto.builder()
                        .code(HttpStatus.OK.value())
                        .message("")
                        .data(reviews)
                        .build());

        // when & then
        mockMvc.perform(get("/api/users/reviews/{userId}", "testId")
                        .param("pageNum", "0")
                        .param("pageSize", "10")
                        .header("Authorization", "Bearer " + "token")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("{class-name}/{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("JWT 토큰")
                        ),
                        queryParameters(
                                parameterWithName("pageNum").description("페이지 번호 (default: 0)"),
                                parameterWithName("pageSize").description("페이지 사이즈 (default: 10)")
                        ),
                        responseFields(
                                fieldWithPath("code").description("상태코드"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("data[].reviewId").description("리뷰 ID"),
                                fieldWithPath("data[].userId").description("유저 ID"),
                                fieldWithPath("data[].itemId").description("상품 ID"),
                                fieldWithPath("data[].itemName").description("상품 이름"),
                                fieldWithPath("data[].itemImage").description("상품 이미지"),
                                fieldWithPath("data[].content").description("리뷰 내용"),
                                fieldWithPath("data[].reviewImage").description("리뷰 이미지"),
                                fieldWithPath("data[].rate").description("리뷰 평점"),
                                fieldWithPath("data[].createdAt").description("리뷰 생성일"),
                                fieldWithPath("data[].updatedAt").description("리뷰 수정일")
                        )
                ));
    }

    @Test
    void updateReview() throws Exception {
        // request
        ReviewUpdateRequestDto reviewUpdateRequestDto = ReviewUpdateRequestDto.builder()
                .userId("testId")
                .content("testContent")
                .image("testImage")
                .rate(5)
                .build();

        // given
        given(reviewService.updateReview(any(), any(), any())).willReturn(
                ResponseDto.builder()
                        .code(HttpStatus.OK.value())
                        .message("")
                        .data(1L)
                        .build());

        // when & then
        mockMvc.perform(patch("/api/users/reviews/{reviewId}", 1L)
                        .header("Authorization", "Bearer " + "token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewUpdateRequestDto)))
                .andExpect(status().isOk())
                .andDo(document("{class-name}/{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("JWT 토큰")
                        ),
                        requestFields(
                                fieldWithPath("userId").description("유저 ID"),
                                fieldWithPath("content").description("리뷰 내용"),
                                fieldWithPath("image").description("리뷰 이미지"),
                                fieldWithPath("rate").description("리뷰 평점")
                        ),
                        responseFields(
                                fieldWithPath("code").description("상태코드"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("data").description("수정한 리뷰 ID")
                        )
                ));
    }

    @Test
    void deleteReview() throws Exception {
        // given
        given(reviewService.deleteReview(any(), any())).willReturn(
                ResponseDto.builder()
                        .code(HttpStatus.OK.value())
                        .message("")
                        .data(1L)
                        .build());

        // when & then
        mockMvc.perform(delete("/api/users/reviews/{reviewId}", 1L)
                        .header("Authorization", "Bearer " + "token")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("{class-name}/{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("JWT 토큰")
                        ),
                        responseFields(
                                fieldWithPath("code").description("상태코드"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("data").description("삭제한 리뷰 ID")
                        )
                ));
    }
}