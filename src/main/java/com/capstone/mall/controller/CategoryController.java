package com.capstone.mall.controller;

import com.capstone.mall.model.ResponseDto;
import com.capstone.mall.model.category.CategoryCreateRequestDto;
import com.capstone.mall.model.category.CategoryUpdateRequestDto;
import com.capstone.mall.service.category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/public/categories")
    public ResponseEntity<ResponseDto> readCategory() {
        ResponseDto responseDto = categoryService.readCategoryList();

        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @PostMapping("/admin/categories")
    public ResponseEntity<ResponseDto> createCategory(@RequestBody CategoryCreateRequestDto categoryRequestDto) {
        ResponseDto responseDto = categoryService.createCategory(categoryRequestDto);

        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @PatchMapping("/admin/categories/{categoryId}")
    public ResponseEntity<ResponseDto> updateCategory(@PathVariable Long categoryId, @RequestBody CategoryUpdateRequestDto categoryRequestDto) {
        ResponseDto responseDto = categoryService.updateCategory(categoryId, categoryRequestDto);

        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<ResponseDto> deleteCategory(@PathVariable Long categoryId) {
        ResponseDto responseDto = categoryService.deleteCategory(categoryId);

        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }
}
