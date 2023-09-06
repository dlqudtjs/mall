package com.capstone.mall.service.category;

import com.capstone.mall.model.ResponseDto;
import com.capstone.mall.model.category.CategoryRequestDto;

public interface CategoryService {

    ResponseDto readCategoryList();

    ResponseDto createCategory(CategoryRequestDto categoryRequestDto);

    ResponseDto updateCategory(Long categoryId, CategoryRequestDto categoryRequestDto);

    ResponseDto deleteCategory(Long categoryId);
}
