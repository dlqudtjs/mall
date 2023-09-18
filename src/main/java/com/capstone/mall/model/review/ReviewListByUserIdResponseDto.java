package com.capstone.mall.model.review;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class ReviewListByUserIdResponseDto {

    List<ReviewListByUserId> reviews;

    int totalPage;
}
