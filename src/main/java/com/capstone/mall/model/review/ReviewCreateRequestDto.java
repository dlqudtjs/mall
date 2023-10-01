package com.capstone.mall.model.review;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
@Getter
public class ReviewCreateRequestDto {

    private Long itemId;
    private String content;
    private int rate;
    private String image;
}
