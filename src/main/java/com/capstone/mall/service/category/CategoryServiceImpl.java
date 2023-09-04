package com.capstone.mall.service.category;

import com.capstone.mall.model.ResponseDto;
import com.capstone.mall.model.category.Category;
import com.capstone.mall.model.category.CategoryResponseDto;
import com.capstone.mall.repository.JpaCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final JpaCategoryRepository categoryRepository;

    @Override
    public ResponseDto readCategoryList() {
        log.info("readCategoryList");

        List<Category> findCategoryList = categoryRepository.findAll();

        List<CategoryResponseDto> categoryList = null;

        return ResponseDto.builder()
                .code(200)
                .message("")
                .data(categoryList)
                .build();
    }
}
