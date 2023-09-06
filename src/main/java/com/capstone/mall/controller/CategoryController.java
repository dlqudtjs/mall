package com.capstone.mall.controller;

import com.capstone.mall.model.ResponseDto;
import com.capstone.mall.model.category.CategoryRequestDto;
import com.capstone.mall.service.category.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/categories")
    public ResponseDto category() {
        log.info("Category Controller");

        return categoryService.readCategoryList();
    }

    @PostMapping("/categories")
    public ResponseDto createCategory(@RequestBody CategoryRequestDto categoryRequestDto) {
        log.info("Category Controller");

        return categoryService.createCategory(categoryRequestDto);
    }

    @PatchMapping("/categories/{categoryId}")
    public ResponseDto updateCategory(@PathVariable Long categoryId, @RequestBody CategoryRequestDto categoryRequestDto) {
        log.info("Category Controller");

        return categoryService.updateCategory(categoryId, categoryRequestDto);
    }

    @DeleteMapping("/categories/{categoryId}")
    public ResponseDto deleteCategory(@PathVariable Long categoryId) {
        log.info("Category Controller");

        return categoryService.deleteCategory(categoryId);
    }
}
