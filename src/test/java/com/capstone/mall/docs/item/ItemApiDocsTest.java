package com.capstone.mall.docs.item;

import com.capstone.mall.controller.ItemController;
import com.capstone.mall.docs.RestDocumentSupport;
import com.capstone.mall.model.ResponseDto;
import com.capstone.mall.model.item.*;
import com.capstone.mall.service.item.ItemService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
public class ItemApiDocsTest extends RestDocumentSupport {

    @MockBean
    private ItemService itemService;

    @Test
    void readItem() throws Exception {
        // request
        ItemResponseDto itemResponseDto = ItemResponseDto.builder()
                .itemId(1L)
                .sellerId("testId")
                .categoryId(1L)
                .name("testName")
                .image1("testImage1")
                .image2("testImage2")
                .image3("testImage3")
                .content("testContent")
                .price(1000)
                .rate(5.0)
                .reviewCount(1)
                .stock(10)
                .build();

        // given
        given(itemService.readItem(any())).willReturn(
                ResponseDto.builder()
                        .code(HttpStatus.CREATED.value())
                        .message("")
                        .data(itemResponseDto)
                        .build());

        // when & then
        mockMvc.perform(get("/api/public/items/{itemId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(document("{class-name}/{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("code").description("상태코드"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("data.itemId").description("상품 ID"),
                                fieldWithPath("data.sellerId").description("판매자 ID"),
                                fieldWithPath("data.categoryId").description("카테고리 ID"),
                                fieldWithPath("data.name").description("상품 이름"),
                                fieldWithPath("data.image1").description("상품 이미지1"),
                                fieldWithPath("data.image2").description("상품 이미지2"),
                                fieldWithPath("data.image3").description("상품 이미지3"),
                                fieldWithPath("data.content").description("상품 설명"),
                                fieldWithPath("data.price").description("상품 가격"),
                                fieldWithPath("data.rate").description("상품 평점"),
                                fieldWithPath("data.reviewCount").description("상품 리뷰 수"),
                                fieldWithPath("data.stock").description("상품 재고")
                        )
                ));
    }

    @Test
    void readItemListBySearch() throws Exception {
        // response
        List<ItemProjection> itemProjectionList = List.of(
                ItemProjection.builder()
                        .itemId(1L)
                        .name("testName")
                        .image1("testImage1")
                        .content("testContent")
                        .price(1000)
                        .stock(10)
                        .reviewCount(1)
                        .createdAt(new Date())
                        .updatedAt(new Date())
                        .avgRating(5.0)
                        .sales(10)
                        .build()
        );

        ItemProjectionListResponseDto itemProjectionListResponseDto = ItemProjectionListResponseDto.builder()
                .items(itemProjectionList)
                .totalPage(1)
                .build();

        // given
        given(itemService.readItemListBySearch(any(), any(Integer.class), any(Integer.class), any(), any())).willReturn(
                ResponseDto.builder()
                        .code(HttpStatus.OK.value())
                        .message("")
                        .data(itemProjectionListResponseDto)
                        .build());

        // when & then
        mockMvc.perform(get("/api/public/items")
                        .param("search", "itemKeyword")
                        .param("sort", "createdAt")
                        .param("sortType", "desc")
                        .param("pageNum", "0")
                        .param("pageSize", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("{class-name}/{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("search").description("검색어 (빈 문자열의 경우 전체 상품 조회)"),
                                parameterWithName("sort").description("정렬 기준 (default: createdAt)"),
                                parameterWithName("sortType").description("정렬 방식 (default: desc)"),
                                parameterWithName("pageNum").description("페이지 번호 (default: 0)"),
                                parameterWithName("pageSize").description("페이지 사이즈 (default: 10)")
                        ),
                        responseFields(
                                fieldWithPath("code").description("상태 코드"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("data.items[].itemId").description("상품 ID"),
                                fieldWithPath("data.items[].name").description("상품 이름"),
                                fieldWithPath("data.items[].image1").description("상품 이미지1"),
                                fieldWithPath("data.items[].content").description("상품 설명"),
                                fieldWithPath("data.items[].price").description("상품 가격"),
                                fieldWithPath("data.items[].stock").description("상품 재고"),
                                fieldWithPath("data.items[].reviewCount").description("상품 리뷰 수"),
                                fieldWithPath("data.items[].createdAt").description("상품 생성 날짜"),
                                fieldWithPath("data.items[].updatedAt").description("상품 수정 날짜"),
                                fieldWithPath("data.items[].avgRating").description("상품 평점"),
                                fieldWithPath("data.items[].sales").description("상품 판매 수"),
                                fieldWithPath("data.totalPage").description("총 페이지 수")
                        )
                ));
    }

    @Test
    void readItemListByCategory() throws Exception {
        // response
        List<ItemProjection> itemProjectionList = List.of(
                ItemProjection.builder()
                        .itemId(1L)
                        .name("testName")
                        .image1("testImage1")
                        .content("testContent")
                        .price(1000)
                        .stock(10)
                        .reviewCount(1)
                        .createdAt(new Date())
                        .updatedAt(new Date())
                        .avgRating(5.0)
                        .sales(10)
                        .build()
        );

        ItemProjectionListResponseDto itemProjectionListResponseDto = ItemProjectionListResponseDto.builder()
                .items(itemProjectionList)
                .totalPage(1)
                .build();

        // given
        given(itemService.readItemListByCategoryId(any(), any(Integer.class), any(Integer.class), any(), any())).willReturn(
                ResponseDto.builder()
                        .code(HttpStatus.OK.value())
                        .message("")
                        .data(itemProjectionListResponseDto)
                        .build());

        // when & then
        mockMvc.perform(get("/api/public/categories/{categoryId}/items", 1L)
                        .param("sort", "createdAt")
                        .param("sortType", "desc")
                        .param("pageNum", "0")
                        .param("pageSize", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("{class-name}/{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("sort").description("정렬 기준 (default: createdAt)"),
                                parameterWithName("sortType").description("정렬 방식 (default: desc)"),
                                parameterWithName("pageNum").description("페이지 번호 (default: 0)"),
                                parameterWithName("pageSize").description("페이지 사이즈 (default: 10)")
                        ),
                        responseFields(
                                fieldWithPath("code").description("상태 코드"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("data.items[].itemId").description("상품 ID"),
                                fieldWithPath("data.items[].name").description("상품 이름"),
                                fieldWithPath("data.items[].image1").description("상품 이미지1"),
                                fieldWithPath("data.items[].content").description("상품 설명"),
                                fieldWithPath("data.items[].price").description("상품 가격"),
                                fieldWithPath("data.items[].stock").description("상품 재고"),
                                fieldWithPath("data.items[].reviewCount").description("상품 리뷰 수"),
                                fieldWithPath("data.items[].createdAt").description("상품 생성 날짜"),
                                fieldWithPath("data.items[].updatedAt").description("상품 수정 날짜"),
                                fieldWithPath("data.items[].avgRating").description("상품 평점"),
                                fieldWithPath("data.items[].sales").description("상품 판매 수"),
                                fieldWithPath("data.totalPage").description("총 페이지 수")
                        )
                ));
    }

    @Test
    void readItemListBySellerId() throws Exception {
        // response
        List<ItemProjection> itemProjectionList = List.of(
                ItemProjection.builder()
                        .itemId(1L)
                        .name("testName")
                        .image1("testImage1")
                        .content("testContent")
                        .price(1000)
                        .stock(10)
                        .reviewCount(1)
                        .createdAt(new Date())
                        .updatedAt(new Date())
                        .avgRating(5.0)
                        .sales(10)
                        .build()
        );

        ItemProjectionListResponseDto itemProjectionListResponseDto = ItemProjectionListResponseDto.builder()
                .items(itemProjectionList)
                .totalPage(1)
                .build();

        // given
        given(itemService.readItemListBySellerId(any(), any(Integer.class), any(Integer.class), any())).willReturn(
                ResponseDto.builder()
                        .code(HttpStatus.OK.value())
                        .message("")
                        .data(itemProjectionListResponseDto)
                        .build());

        // when & then
        mockMvc.perform(get("/api/sellers/items/{userId}", "testId")
                        .header("Authorization", "Bearer " + "token")
                        .param("pageNum", "0")
                        .param("pageSize", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("{class-name}/{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("JWT 토큰")
                        ),
                        queryParameters(
                                parameterWithName("pageNum").description("페이지 번호 (default: 0)"),
                                parameterWithName("pageSize").description("페이지 사이즈 (default: 10)")
                        ),
                        responseFields(
                                fieldWithPath("code").description("상태 코드"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("data.items[].itemId").description("상품 ID"),
                                fieldWithPath("data.items[].name").description("상품 이름"),
                                fieldWithPath("data.items[].image1").description("상품 이미지1"),
                                fieldWithPath("data.items[].content").description("상품 설명"),
                                fieldWithPath("data.items[].price").description("상품 가격"),
                                fieldWithPath("data.items[].stock").description("상품 재고"),
                                fieldWithPath("data.items[].reviewCount").description("상품 리뷰 수"),
                                fieldWithPath("data.items[].createdAt").description("상품 생성 날짜"),
                                fieldWithPath("data.items[].updatedAt").description("상품 수정 날짜"),
                                fieldWithPath("data.items[].avgRating").description("상품 평점"),
                                fieldWithPath("data.items[].sales").description("상품 판매 수"),
                                fieldWithPath("data.totalPage").description("총 페이지 수")
                        )
                ));
    }

    @Test
    void createItem() throws Exception {
        // request
        ItemCreateRequestDto itemCreateRequestDto = ItemCreateRequestDto.builder()
                .name("testName")
                .categoryId(1L)
                .price(1000)
                .stock(10)
                .content("testContent")
                .image1("testImage1")
                .image2("testImage2")
                .image3("testImage3")
                .keywords(List.of(
                        "testKeyword1",
                        "testKeyword2"
                ))
                .build();

        // given
        given(itemService.createItem(any(), any())).willReturn(
                ResponseDto.builder()
                        .code(HttpStatus.CREATED.value())
                        .message("")
                        .data(1L)
                        .build());

        // when & then
        mockMvc.perform(post("/api/sellers/items/{userId}", "testId")
                        .header("Authorization", "Bearer " + "token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemCreateRequestDto)))
                .andExpect(status().isCreated())
                .andDo(document("{class-name}/{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("JWT 토큰")
                        ),
                        requestFields(
                                fieldWithPath("name").description("상품 이름"),
                                fieldWithPath("categoryId").description("카테고리 ID"),
                                fieldWithPath("price").description("상품 가격"),
                                fieldWithPath("stock").description("상품 재고"),
                                fieldWithPath("content").description("상품 설명"),
                                fieldWithPath("image1").description("상품 이미지1"),
                                fieldWithPath("image2").description("상품 이미지2"),
                                fieldWithPath("image3").description("상품 이미지3"),
                                fieldWithPath("keywords").description("상품 키워드")
                        ),
                        responseFields(
                                fieldWithPath("code").description("상태코드"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("data").description("등록한 상품 ID")
                        )
                ));
    }

    @Test
    void updateItem() throws Exception {
        // request
        ItemUpdateRequestDto itemUpdateRequestDto = ItemUpdateRequestDto.builder()
                .sellerId("testId")
                .name("testName")
                .categoryId(1L)
                .price(1000)
                .stock(10)
                .content("testContent")
                .image1("testImage1")
                .image2("testImage2")
                .image3("testImage3")
                .build();

        // given
        given(itemService.updateItem(any(), any(), any())).willReturn(
                ResponseDto.builder()
                        .code(HttpStatus.CREATED.value())
                        .message("")
                        .data(1L)
                        .build());

        // when & then
        mockMvc.perform(patch("/api/sellers/items/{itemId}", 1L)
                        .header("Authorization", "Bearer " + "token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemUpdateRequestDto)))
                .andExpect(status().isCreated())
                .andDo(document("{class-name}/{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("JWT 토큰")
                        ),
                        requestFields(
                                fieldWithPath("sellerId").description("판매자 ID"),
                                fieldWithPath("name").description("상품 이름"),
                                fieldWithPath("categoryId").description("카테고리 ID"),
                                fieldWithPath("price").description("상품 가격"),
                                fieldWithPath("stock").description("상품 재고"),
                                fieldWithPath("content").description("상품 설명"),
                                fieldWithPath("image1").description("상품 이미지1"),
                                fieldWithPath("image2").description("상품 이미지2"),
                                fieldWithPath("image3").description("상품 이미지3")
                        ),
                        responseFields(
                                fieldWithPath("code").description("상태코드"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("data").description("수정한 상품 ID")
                        )
                ));
    }

    @Test
    void deleteItem() throws Exception {
        // given
        given(itemService.deleteItem(any(), any())).willReturn(
                ResponseDto.builder()
                        .code(HttpStatus.OK.value())
                        .message("")
                        .data(1L)
                        .build());

        // when & then
        mockMvc.perform(delete("/api/sellers/items/{itemId}", 1L)
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
                                fieldWithPath("data").description("삭제한 상품 ID")
                        )
                ));
    }
}