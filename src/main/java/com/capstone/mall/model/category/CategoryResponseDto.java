package com.capstone.mall.model.category;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class CategoryResponseDto {

    private Long categoryId;

    private String name;

    private String status;

    List<CategoryResponseDto> child;
}
