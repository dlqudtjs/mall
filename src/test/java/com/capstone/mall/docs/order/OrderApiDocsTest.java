package com.capstone.mall.docs.order;

import com.capstone.mall.controller.OrderController;
import com.capstone.mall.docs.RestDocumentSupport;
import com.capstone.mall.model.ResponseDto;
import com.capstone.mall.model.order.OrderListResponseDto;
import com.capstone.mall.model.order.OrderResponseDto;
import com.capstone.mall.model.order.OrderUpdateRequestDto;
import com.capstone.mall.model.order.orderDetail.OrderDetailListResponseDto;
import com.capstone.mall.model.order.orderDetail.OrderDetailResponseDto;
import com.capstone.mall.model.order.orderForm.OrderFormDetail;
import com.capstone.mall.service.order.OrderService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OrderController.class)
public class OrderApiDocsTest extends RestDocumentSupport {

    @MockBean
    private OrderService orderService;

    @Test
    void createOrderForm() throws Exception {
        // response List
        List<OrderFormDetail> orderFormDetails = List.of(
                OrderFormDetail.builder()
                        .itemId(1L)
                        .itemName("testName")
                        .image("testImage")
                        .quantity(1)
                        .price(1000)
                        .build(),
                OrderFormDetail.builder()
                        .itemId(2L)
                        .itemName("testName")
                        .image("testImage")
                        .quantity(1)
                        .price(1000)
                        .build()
        );

        // given
        given(orderService.createOrderForm(any(), any())).willReturn(
                ResponseDto.builder()
                        .code(HttpStatus.CREATED.value())
                        .message("")
                        .data(orderFormDetails)
                        .build());

        // when & then
        mockMvc.perform(get("/api/users/form/orders/{userId}", "testId")
                        .header("Authorization", "Bearer " + "token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("items", "1:1,2:1"))
                .andExpect(status().isCreated())
                .andDo(document("{class-name}/{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("JWT 토큰")
                        ),
                        queryParameters(
                                parameterWithName("items").description("주문할 상품 ID와 수량 (ex. 1:1,2:1)")
                        ),
                        responseFields(
                                fieldWithPath("code").description("상태코드"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("data[].itemId").description("상품 ID"),
                                fieldWithPath("data[].itemName").description("상품 이름"),
                                fieldWithPath("data[].image").description("상품 이미지"),
                                fieldWithPath("data[].quantity").description("상품 수량"),
                                fieldWithPath("data[].price").description("상품 가격")
                        )
                ));
    }

    @Test
    void updateOrder() throws Exception {
        // request
        OrderUpdateRequestDto orderUpdateRequestDto = new OrderUpdateRequestDto();
        orderUpdateRequestDto.setResult(1);

        // given
        given(orderService.updateOrder(any(), any(), any())).willReturn(
                ResponseDto.builder()
                        .code(HttpStatus.OK.value())
                        .message("")
                        .data(1L)
                        .build());

        // when & then
        mockMvc.perform(patch("/api/sellers/orders/{orderDetailId}", 1L)
                        .header("Authorization", "Bearer " + "token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderUpdateRequestDto)))
                .andExpect(status().isOk())
                .andDo(document("{class-name}/{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("JWT 토큰")
                        ),
                        requestFields(
                                fieldWithPath("result").description("주문 상태 (주문 상태 코드 참고)")
                        ),
                        responseFields(
                                fieldWithPath("code").description("상태 코드"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("data").description("수정한 주문 상세 ID")
                        )
                ));
    }

    @Test
    void getSoldOrderList() throws Exception {
        // response
        List<OrderDetailResponseDto> orderDetailResponseDtoList = List.of(
                OrderDetailResponseDto.builder()
                        .orderDetailId(1L)
                        .orderId(1L)
                        .itemId(1L)
                        .itemName("testName")
                        .sellerId("testId")
                        .image("testImage")
                        .price(1000)
                        .quantity(1)
                        .result(1)
                        .orderDate(new Date())
                        .build(),
                OrderDetailResponseDto.builder()
                        .orderDetailId(2L)
                        .orderId(2L)
                        .itemId(2L)
                        .itemName("testName")
                        .sellerId("testId")
                        .image("testImage")
                        .price(1000)
                        .quantity(1)
                        .result(1)
                        .orderDate(new Date())
                        .build()
        );

        OrderDetailListResponseDto orderDetailListResponseDto = OrderDetailListResponseDto.builder()
                .orders(orderDetailResponseDtoList)
                .totalPage(1)
                .build();

        // given
        given(orderService.getSoldOrderList(any(), any(Integer.class), any(Integer.class), any())).willReturn(
                ResponseDto.builder()
                        .code(HttpStatus.OK.value())
                        .message("")
                        .data(orderDetailListResponseDto)
                        .build());

        // when & then
        mockMvc.perform(get("/api/sellers/orders/{userId}", "testId")
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
                                parameterWithName("pageNum").description("페이지 번호"),
                                parameterWithName("pageSize").description("페이지 사이즈")
                        ),
                        responseFields(
                                fieldWithPath("code").description("상태코드"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("data.orders[].orderDetailId").description("주문 상세 ID"),
                                fieldWithPath("data.orders[].orderId").description("주문 ID"),
                                fieldWithPath("data.orders[].itemId").description("상품 ID"),
                                fieldWithPath("data.orders[].itemName").description("상품 이름"),
                                fieldWithPath("data.orders[].sellerId").description("판매자 ID"),
                                fieldWithPath("data.orders[].image").description("상품 이미지"),
                                fieldWithPath("data.orders[].price").description("상품 가격"),
                                fieldWithPath("data.orders[].quantity").description("상품 수량"),
                                fieldWithPath("data.orders[].result").description("주문 상태"),
                                fieldWithPath("data.orders[].orderDate").description("주문 날짜"),
                                fieldWithPath("data.totalPage").description("총 페이지 수")
                        )
                ));
    }

    @Test
    void getPurchaseList() throws Exception {
        // response
        List<OrderResponseDto> orderResponseDtoList = List.of(
                OrderResponseDto.builder()
                        .orderId(1L)
                        .orderDate(new Date())
                        .totalPrice(5000)
                        .orderDetails(List.of(
                                OrderDetailResponseDto.builder()
                                        .orderDetailId(1L)
                                        .orderId(1L)
                                        .itemId(1L)
                                        .itemName("testName")
                                        .sellerId("testId")
                                        .image("testImage")
                                        .price(1000)
                                        .quantity(1)
                                        .result(1)
                                        .orderDate(new Date())
                                        .build(),
                                OrderDetailResponseDto.builder()
                                        .orderDetailId(2L)
                                        .orderId(1L)
                                        .itemId(2L)
                                        .itemName("testName")
                                        .sellerId("testId")
                                        .image("testImage")
                                        .price(1000)
                                        .quantity(1)
                                        .result(1)
                                        .orderDate(new Date())
                                        .build()
                        ))
                        .build()
        );

        OrderListResponseDto orderListResponseDto = OrderListResponseDto.builder()
                .orders(orderResponseDtoList)
                .totalPage(1)
                .build();

        // given
        given(orderService.getPurchaseList(any(), any(Integer.class), any(Integer.class), any())).willReturn(
                ResponseDto.builder()
                        .code(HttpStatus.OK.value())
                        .message("")
                        .data(orderListResponseDto)
                        .build());

        // when & then
        mockMvc.perform(get("/api/users/orders/{userId}", "testId")
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
                                parameterWithName("pageNum").description("페이지 번호"),
                                parameterWithName("pageSize").description("페이지 사이즈")
                        ),
                        responseFields(
                                fieldWithPath("code").description("상태코드"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("data.orders[].orderId").description("주문 ID"),
                                fieldWithPath("data.orders[].orderDate").description("주문 날짜"),
                                fieldWithPath("data.orders[].totalPrice").description("총 가격"),
                                fieldWithPath("data.orders[].orderDetails[].orderDetailId").description("주문 상세 ID"),
                                fieldWithPath("data.orders[].orderDetails[].orderId").description("주문 ID"),
                                fieldWithPath("data.orders[].orderDetails[].itemId").description("상품 ID"),
                                fieldWithPath("data.orders[].orderDetails[].itemName").description("상품 이름"),
                                fieldWithPath("data.orders[].orderDetails[].sellerId").description("판매자 ID"),
                                fieldWithPath("data.orders[].orderDetails[].image").description("상품 이미지"),
                                fieldWithPath("data.orders[].orderDetails[].price").description("상품 가격"),
                                fieldWithPath("data.orders[].orderDetails[].quantity").description("상품 수량"),
                                fieldWithPath("data.orders[].orderDetails[].result").description("주문 상태"),
                                fieldWithPath("data.orders[].orderDetails[].orderDate").description("주문 날짜"),
                                fieldWithPath("data.totalPage").description("총 페이지 수")
                        )
                ));
    }
}