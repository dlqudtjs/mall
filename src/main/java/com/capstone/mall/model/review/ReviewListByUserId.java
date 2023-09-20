package com.capstone.mall.model.review;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Builder
@Getter
@Setter
public class ReviewListByUserId {

    private Long reviewId;

    private String userId;

    private Long itemId;

    private String itemName;

    private String itemImage;

    private String content;

    private String reviewImage;

    private int rate;

    private Date createdAt;

    private Date updatedAt;
}
