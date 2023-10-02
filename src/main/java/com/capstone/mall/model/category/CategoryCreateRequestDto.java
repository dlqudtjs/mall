package com.capstone.mall.model.category;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CategoryCreateRequestDto {

    private Long parentId;

    private String name;

    private String status;
}
