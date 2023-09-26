package com.capstone.mall.model.item;

import java.util.Date;

public interface ItemProjectionInterface {

    Long getItemId();

    String getSellerId();

    Long getCategoryId();

    String getName();

    String getImage1();

    String getImage2();

    String getImage3();

    String getContent();

    Date getCreatedAt();

    Date getUpdatedAt();

    int getPrice();

    int getStock();

    Integer getReviewCount();

    Double getAvgRating();

    Integer getSales();
}