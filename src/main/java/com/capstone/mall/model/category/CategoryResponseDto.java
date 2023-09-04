package com.capstone.mall.model.category;

import lombok.Builder;

import java.util.List;

@Builder
public class CategoryResponseDto {

    private Long categoryId;

    private String name;

    private String Status;

    List<CategoryResponseDto> child;
}
