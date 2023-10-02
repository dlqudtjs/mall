package com.capstone.mall.service.category;

import com.capstone.mall.model.ResponseDto;
import com.capstone.mall.model.category.CategoryCreateRequestDto;

public interface CategoryService {

    ResponseDto readCategoryList();

    ResponseDto createCategory(CategoryCreateRequestDto categoryRequestDto);

    ResponseDto updateCategory(Long categoryId, CategoryCreateRequestDto categoryRequestDto);

    ResponseDto deleteCategory(Long categoryId);
}
