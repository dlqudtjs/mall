package com.capstone.mall.model.review;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
@Getter
public class ReviewUpdateRequestDto {

    private String userId;
    private String content;
    private int rate;
    private String image;
}
