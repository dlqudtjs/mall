package com.capstone.mall.docs.category;

import com.capstone.mall.controller.CategoryController;
import com.capstone.mall.docs.RestDocumentSupport;
import com.capstone.mall.model.ResponseDto;
import com.capstone.mall.model.category.CategoryCreateRequestDto;
import com.capstone.mall.model.category.CategoryResponseDto;
import com.capstone.mall.model.category.CategoryUpdateRequestDto;
import com.capstone.mall.service.category.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CategoryController.class)
public class CategoryApiDocsTest extends RestDocumentSupport {

    @MockBean
    private CategoryService categoryService;

    @Test
    void readCategory() throws Exception {
        // response List
        List<CategoryResponseDto> categories = List.of(
                CategoryResponseDto.builder()
                        .categoryId(1L)
                        .name("testName")
                        .status("ACTIVE")
                        .child(List.of(
                                CategoryResponseDto.builder()
                                        .categoryId(2L)
                                        .name("testName2")
                                        .status("ACTIVE")
                                        .child(List.of(
                                                CategoryResponseDto.builder()
                                                        .categoryId(3L)
                                                        .name("testName3")
                                                        .status("ACTIVE")
                                                        .child(null)
                                                        .build(),
                                                CategoryResponseDto.builder()
                                                        .categoryId(4L)
                                                        .name("testName4")
                                                        .status("ACTIVE")
                                                        .child(null)
                                                        .build()
                                        ))
                                        .build()
                        ))
                        .build()
        );

        // given
        given(categoryService.readCategoryList()).willReturn(
                ResponseDto.builder()
                        .code(HttpStatus.OK.value())
                        .message("")
                        .data(categories)
                        .build());

        // when & then
        mockMvc.perform(get("/api/public/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("{class-name}/{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("code").description("상태코드"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("data[].categoryId").description("카테고리 ID"),
                                fieldWithPath("data[].name").description("카테고리 이름"),
                                fieldWithPath("data[].status").description("카테고리 상태"),
                                fieldWithPath("data[].child").description("자식 카테고리"),
                                fieldWithPath("data[].child[].categoryId").description("자식 카테고리 ID"),
                                fieldWithPath("data[].child[].name").description("자식 카테고리 이름"),
                                fieldWithPath("data[].child[].status").description("자식 카테고리 상태"),
                                fieldWithPath("data[].child[].child").description("자식 카테고리"),
                                fieldWithPath("data[].child[].child[].categoryId").description("자식 카테고리 ID"),
                                fieldWithPath("data[].child[].child[].name").description("자식 카테고리 이름"),
                                fieldWithPath("data[].child[].child[].status").description("자식 카테고리 상태"),
                                fieldWithPath("data[].child[].child[].child").description("자식 카테고리")
                        )
                ));
    }

    @Test
    void createCategory() throws Exception {
        // request
        CategoryCreateRequestDto categoryRequestDto = CategoryCreateRequestDto.builder()
                .categoryId(1L)
                .parentId(1L)
                .name("testName")
                .status("ACTIVE")
                .build();

        // given
        given(categoryService.createCategory(any())).willReturn(
                ResponseDto.builder()
                        .code(HttpStatus.CREATED.value())
                        .message("")
                        .data(1L)
                        .build());

        // when & then
        mockMvc.perform(post("/api/admin/categories")
                        .header("Authorization", "Bearer " + "token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryRequestDto)))
                .andExpect(status().isCreated())
                .andDo(document("{class-name}/{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("JWT 토큰")
                        ),
                        requestFields(
                                fieldWithPath("categoryId").description("카테고리 ID"),
                                fieldWithPath("parentId").description("부모 카테고리 ID"),
                                fieldWithPath("name").description("카테고리 이름"),
                                fieldWithPath("status").description("카테고리 상태")
                        ),
                        responseFields(
                                fieldWithPath("code").description("상태 코드"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("data").description("생성한 카테고리 ID")
                        )
                ));
    }

    @Test
    void updateCategory() throws Exception {
        // request
        CategoryUpdateRequestDto categoryRequestDto = CategoryUpdateRequestDto.builder()
                .name("testName")
                .status("ACTIVE")
                .build();

        // given
        given(categoryService.updateCategory(any(), any())).willReturn(
                ResponseDto.builder()
                        .code(HttpStatus.OK.value())
                        .message("")
                        .data(1L)
                        .build());

        // when & then
        mockMvc.perform(patch("/api/admin/categories/{categoryId}", 1L)
                        .header("Authorization", "Bearer " + "token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryRequestDto)))
                .andExpect(status().isOk())
                .andDo(document("{class-name}/{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("JWT 토큰")
                        ),
                        requestFields(
                                fieldWithPath("name").description("카테고리 이름"),
                                fieldWithPath("status").description("카테고리 상태")
                        ),
                        responseFields(
                                fieldWithPath("code").description("상태코드"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("data").description("수정한 카테고리 ID")
                        )
                ));
    }

    @Test
    void deleteCategory() throws Exception {
        // given
        given(categoryService.deleteCategory(any())).willReturn(
                ResponseDto.builder()
                        .code(HttpStatus.OK.value())
                        .message("")
                        .data(1L)
                        .build());

        // when & then
        mockMvc.perform(delete("/api/admin/categories/{categoryId}", 1L)
                        .header("Authorization", "Bearer " + "token")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("{class-name}/{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("JWT 토큰")
                        ),
                        responseFields(
                                fieldWithPath("code").description("상태코드"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("data").description("삭제한 카테고리 ID")
                        )
                ));
    }
}