package com.capstone.mall.controller;

import com.capstone.mall.model.ResponseDto;
import com.capstone.mall.model.category.CategoryRequestDto;
import com.capstone.mall.service.category.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/public/categories")
    public ResponseEntity<ResponseDto> readCategory() {
        ResponseDto responseDto = categoryService.readCategoryList();

        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @PostMapping("/admin/categories")
    public ResponseDto createCategory(@RequestBody CategoryRequestDto categoryRequestDto) {
        return categoryService.createCategory(categoryRequestDto);
    }

    @PatchMapping("/admin/categories/{categoryId}")
    public ResponseDto updateCategory(@PathVariable Long categoryId, @RequestBody CategoryRequestDto categoryRequestDto) {
        return categoryService.updateCategory(categoryId, categoryRequestDto);
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseDto deleteCategory(@PathVariable Long categoryId) {
        return categoryService.deleteCategory(categoryId);
    }
}
