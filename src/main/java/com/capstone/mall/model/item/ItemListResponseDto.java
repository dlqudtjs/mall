//package com.capstone.mall.model.item;
//
//import jakarta.persistence.Column;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.RequiredArgsConstructor;
//
//import java.util.Date;
//
//@Builder
//@Data
//@AllArgsConstructor
//@RequiredArgsConstructor
///*
// ItemListResponseDto 는 프로시저를 통해 검색된 아이템들의 정보를 담는 DTO 임.
// */
//public class ItemListResponseDto {
//
//    private Long itemId;
//
//    @Column(name = "seller_id")
//    private String sellerId;
//
//    @Column(name = "category_id")
//    private Long categoryId;
//
//    private String name;
//
//    @Column(name = "image_1")
//    private String image1;
//
//    @Column(name = "image_2")
//    private String image2;
//
//    @Column(name = "image_3")
//    private String image3;
//
//    private String content;
//
//    private int price;
//
//    private int stock;
//
//    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
//    private Date createdAt;
//
//    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
//    private Date updatedAt;
//
//    @Column(name = "item_review_count")
//    private int itemReviewCount;
//
//    @Column(name = "item_avg_review")
//    private int itemAvgReview;
//
//    private int sales;
//}
