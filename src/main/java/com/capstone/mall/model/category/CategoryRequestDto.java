package com.capstone.mall.model.category;

import lombok.Getter;

@Getter
public class CategoryRequestDto {

    private Long parentId;

    private String name;

    private String status;
}
