package com.capstone.mall.service.category;

import com.capstone.mall.model.ResponseDto;
import com.capstone.mall.model.category.CategoryCreateRequestDto;
import com.capstone.mall.model.category.CategoryUpdateRequestDto;

public interface CategoryService {

    ResponseDto readCategoryList();

    ResponseDto createCategory(CategoryCreateRequestDto categoryRequestDto);

    ResponseDto updateCategory(Long categoryId, CategoryUpdateRequestDto categoryRequestDto);

    ResponseDto deleteCategory(Long categoryId);
}
