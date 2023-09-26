package com.capstone.mall.docs.cart;

import com.capstone.mall.controller.CartController;
import com.capstone.mall.docs.RestDocumentSupport;
import com.capstone.mall.model.ResponseDto;
import com.capstone.mall.model.cart.CartRequestDto;
import com.capstone.mall.model.cart.CartResponseDto;
import com.capstone.mall.model.cart.CartUpdateRequestDto;
import com.capstone.mall.service.cart.CartService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CartController.class)
public class CartApiDocsTest extends RestDocumentSupport {

    @MockBean
    private CartService cartService;

    @Test
    void addCart() throws Exception {
        final String userId = "1";
        final Long itemId = 1L;
        final int quantity = 1;

        // given
        CartRequestDto cartRequestDto = CartRequestDto.builder()
                .itemId(itemId)
                .quantity(quantity)
                .build();

        given(cartService.addCart(eq(userId), any())).willReturn(
                ResponseDto.builder()
                        .code(HttpStatus.OK.value())
                        .message("")
                        .data(itemId)
                        .build());

        // when & then
        mockMvc.perform(post("/api/users/carts/{userId}", userId)
                        .header("Authorization", "Bearer " + "token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartRequestDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("{class-name}/{method-name}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestHeaders(
                                        headerWithName("Authorization").description("JWT 토큰")
                                ),
                                requestFields(

                                        fieldWithPath("itemId").description("상품 ID"),
                                        fieldWithPath("quantity").description("상품 개수")
                                ),
                                responseFields(
                                        fieldWithPath("code").description("상태 코드"),
                                        fieldWithPath("message").description("메시지"),
                                        fieldWithPath("data").description("추가한 카트 ID")
                                )
                        )
                );
    }

    @Test
    void readCartList() throws Exception {
        final String userId = "1";

        // given
        List<CartResponseDto> carts = new ArrayList<>();
        carts.add(CartResponseDto.builder()
                .cartId(1L)
                .itemId(1L)
                .name("name1")
                .quantity(1)
                .price(1000)
                .image1("image1")
                .stock(10)
                .build());
        carts.add(CartResponseDto.builder()
                .cartId(2L)
                .itemId(2L)
                .name("name2")
                .quantity(2)
                .price(2000)
                .image1("image2")
                .stock(20)
                .build());

        given(cartService.readCartList(eq(userId), any())).willReturn(
                ResponseDto.builder()
                        .code(HttpStatus.OK.value())
                        .message("")
                        .data(carts)
                        .build());

        // when & then
        mockMvc.perform(get("/api/users/carts/{userId}", userId)
                        .header("Authorization", "Bearer " + "token")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("{class-name}/{method-name}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestHeaders(
                                        headerWithName("Authorization").description("JWT 토큰")
                                ),
                                responseFields(
                                        fieldWithPath("code").description("상태 코드"),
                                        fieldWithPath("message").description("메시지"),
                                        fieldWithPath("data").description("카트 리스트"),
                                        fieldWithPath("data[].cartId").description("카트 ID"),
                                        fieldWithPath("data[].itemId").description("상품 ID"),
                                        fieldWithPath("data[].name").description("상품 이름"),
                                        fieldWithPath("data[].quantity").description("상품 개수"),
                                        fieldWithPath("data[].price").description("상품 가격"),
                                        fieldWithPath("data[].image1").description("상품 이미지"),
                                        fieldWithPath("data[].stock").description("상품 재고")
                                )
                        )
                );
    }

    @Test
    void updateCart() throws Exception {
        final Long cartId = 1L;
        final int quantity = 1;

        CartUpdateRequestDto cartUpdateRequestDto = new CartUpdateRequestDto();
        cartUpdateRequestDto.setQuantity(quantity);

        given(cartService.updateCart(eq(cartId), any(), any())).willReturn(
                ResponseDto.builder()
                        .code(HttpStatus.OK.value())
                        .message("")
                        .data(cartId)
                        .build());

        // when & then
        mockMvc.perform(patch("/api/users/carts/{cartId}", cartId)
                        .header("Authorization", "Bearer " + "token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartUpdateRequestDto)))
                .andExpect(status().isOk())
                .andDo(document("{class-name}/{method-name}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestHeaders(
                                        headerWithName("Authorization").description("JWT 토큰")
                                ),
                                requestFields(
                                        fieldWithPath("quantity").description("상품 개수")
                                ),
                                responseFields(
                                        fieldWithPath("code").description("상태 코드"),
                                        fieldWithPath("message").description("메시지"),
                                        fieldWithPath("data").description("추가한 카트 ID")
                                )
                        )
                );
    }

    @Test
    void deleteCart() throws Exception {
        final Long cartId = 1L;

        given(cartService.deleteCart(eq(cartId), any())).willReturn(
                ResponseDto.builder()
                        .code(HttpStatus.OK.value())
                        .message("")
                        .data(cartId)
                        .build());

        // when & then
        mockMvc.perform(delete("/api/users/carts/{cartId}", cartId)
                        .header("Authorization", "Bearer " + "token")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("{class-name}/{method-name}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestHeaders(
                                        headerWithName("Authorization").description("JWT 토큰")
                                ),
                                responseFields(
                                        fieldWithPath("code").description("상태 코드"),
                                        fieldWithPath("message").description("메시지"),
                                        fieldWithPath("data").description("카트 리스트")
                                )
                        )
                );
    }
}
