package com.capstone.mall.service.category;

import com.capstone.mall.configuration.NoLogging;
import com.capstone.mall.model.ResponseDto;
import com.capstone.mall.model.category.Category;
import com.capstone.mall.model.category.CategoryCreateRequestDto;
import com.capstone.mall.model.category.CategoryResponseDto;
import com.capstone.mall.model.category.CategoryUpdateRequestDto;
import com.capstone.mall.repository.JpaCategoryRepository;
import com.capstone.mall.service.response.ResponseServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final JpaCategoryRepository categoryRepository;
    private final ResponseServiceImpl responseService;

    @Override
    @Transactional
    public ResponseDto readCategoryList() {
        Optional<Category> rootCategory = categoryRepository.findByCategoryId(0L);

        if (rootCategory.isEmpty()) {
            return responseService.createResponseDto(200, "", null);
        }

        List<CategoryResponseDto> categoryResponseDtoList = new ArrayList<>();
        categoryResponseDtoList.add(
                CategoryResponseDto.builder()
                        .categoryId(rootCategory.get().getCategoryId())
                        .name(rootCategory.get().getName())
                        .status(rootCategory.get().getStatus())
                        // 현재 카테고리(ROOT)의 자식 카테고리를 가져옴
                        .child(getChildCategoryList(rootCategory.get().getCategoryId()))
                        .build());

        return responseService.createResponseDto(200, "", categoryResponseDtoList);
    }

    @Override
    public ResponseDto createCategory(CategoryCreateRequestDto categoryRequestDto) {
        if (categoryRepository.existsById(categoryRequestDto.getCategoryId())) {
            return responseService.createResponseDto(200, "categoryId already exists", null);
        }

        Category createCategory = Category.builder()
                .categoryId(categoryRequestDto.getCategoryId())
                .parentCategoryId(categoryRequestDto.getParentId())
                .name(categoryRequestDto.getName())
                .status(categoryRequestDto.getStatus())
                .build();

        Category savedCategory = categoryRepository.save(createCategory);

        return responseService.createResponseDto(201, "", savedCategory.getCategoryId());
    }

    @Override
    public ResponseDto updateCategory(Long categoryId, CategoryUpdateRequestDto categoryRequestDto) {
        Optional<Category> updateCategory = categoryRepository.findByCategoryId(categoryId);

        if (updateCategory.isEmpty()) {
            return responseService.createResponseDto(200, "category does not exist", null);
        }

        updateCategory.get().setName(categoryRequestDto.getName());
        updateCategory.get().setStatus(categoryRequestDto.getStatus());

        return responseService.createResponseDto(200, "", categoryId);
    }

    @Override
    public ResponseDto deleteCategory(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            return responseService.createResponseDto(200, "Category does not exist", null);
        }

        categoryRepository.deleteById(categoryId);

        return responseService.createResponseDto(200, "", categoryId);
    }

    /*
     * @param parent_category_id
     * 자신의 자식 카테고리를 불러오는 재귀 함수
     */
    @NoLogging
    private List<CategoryResponseDto> getChildCategoryList(Long parent_category_id) {
        // 인수로 넘어온 parent_category_id를 부모 카테고리로 가지는 자식 카테고리를 조회함.
        List<Category> categoryList = categoryRepository.findByParentCategoryId(parent_category_id);

        if (categoryList.isEmpty()) {
            return null;
        }

        // 현재 카테고리의 child 필드에 담을 자식 카테고리 리스트 생성
        List<CategoryResponseDto> childCategoryResponseDtoList = new ArrayList<>();

        for (Category category : categoryList) {
            childCategoryResponseDtoList.add(
                    CategoryResponseDto.builder()
                            .categoryId(category.getCategoryId())
                            .name(category.getName())
                            .status(category.getStatus())
                            // 현재 카테고리의 자식 카테고리를 불러와 child 필드에 담음
                            .child(getChildCategoryList(category.getCategoryId()))
                            .build());
        }

        return childCategoryResponseDtoList;
    }
}
