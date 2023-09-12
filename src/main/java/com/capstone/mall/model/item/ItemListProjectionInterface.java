package com.capstone.mall.model.item;

import java.util.Date;
import java.util.Optional;

public interface ItemListProjectionInterface {

    Long getItemId();

    String getName();

    String getImage1();

    String getContent();

    Date getCreatedAt();

    Date getUpdatedAt();

    int getPrice();

    int getStock();

    Optional<Integer> getItemReviewCount();

    Optional<Double> getItemAvgReview();

    int getSales();
}