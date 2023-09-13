package com.capstone.mall.model.item;

public interface ItemProjectionInterface {

    Long getItemId();

    String getSellerId();

    Long getCategoryId();

    String getName();

    String getImage1();

    String getImage2();

    String getImage3();

    String getContent();

    Integer getPrice();

    Double getItemAvgReview();

    Integer getItemReviewCount();

    int getStock();
}