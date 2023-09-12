package com.capstone.mall.model.review;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class ReviewRequestDto {

    private Long itemId;
    private String userId;
    private String content;
    private int rate;
    private String image;
}
