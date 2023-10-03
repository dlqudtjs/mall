package com.capstone.mall.model.review;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ReviewUpdateRequestDto {

    private String userId;
    private String content;
    private int rate;
    private String image;
}
