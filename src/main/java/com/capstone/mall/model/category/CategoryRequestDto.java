package com.capstone.mall.model.category;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryRequestDto {

    private Long parentId;

    private String name;

    private String status;
}
