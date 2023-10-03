package com.capstone.mall.model.review;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ReviewCreateRequestDto {

    private Long itemId;
    private String content;
    private int rate;
    private String image;
}
