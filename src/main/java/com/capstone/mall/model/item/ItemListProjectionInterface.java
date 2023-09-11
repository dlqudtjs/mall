package com.capstone.mall.model.item;

import java.util.Date;

public interface ItemListProjectionInterface {

    Long getItemId();

    String getName();

    String getImage1();

    String getContent();

    Date getCreatedAt();

    Date getUpdatedAt();

    int getPrice();

    int getStock();

    int getItemReviewCount();

    int getItemAvgReview();

    int getSales();
}