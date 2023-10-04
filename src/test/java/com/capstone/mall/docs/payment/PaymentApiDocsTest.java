package com.capstone.mall.docs.payment;

import com.capstone.mall.controller.PaymentController;
import com.capstone.mall.docs.RestDocumentSupport;
import com.capstone.mall.model.ResponseDto;
import com.capstone.mall.model.payment.PaymentRequestDto;
import com.capstone.mall.service.payment.PaymentService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PaymentController.class)
public class PaymentApiDocsTest extends RestDocumentSupport {

    @MockBean
    private PaymentService paymentService;

    @Test
    void payment() throws Exception {
        PaymentRequestDto paymentRequestDto = PaymentRequestDto.builder()
                .recipient("testRecipient")
                .address("testAddress")
                .detailAddress("testDetailAddress")
                .phone("testPhone")
                .zipCode(123456)
                .build();

        // given
        given(paymentService.payment(any(), any(), any())).willReturn(
                ResponseDto.builder()
                        .code(HttpStatus.OK.value())
                        .message("")
                        .data(1L)
                        .build());

        // when & then
        mockMvc.perform(post("/api/users/payments/{userId}", "testId")
                        .header("Authorization", "Bearer " + "token")
                        .param("items", "1:1,2:1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentRequestDto)))
                .andExpect(status().isOk())
                .andDo(document("{class-name}/{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer 토큰")
                        ),
                        queryParameters(
                                parameterWithName("items").description("주문할 상품 ID와 수량 (ex. 1:1,2:1)")
                        ),
                        requestFields(
                                fieldWithPath("recipient").description("수령인"),
                                fieldWithPath("address").description("주소"),
                                fieldWithPath("detailAddress").description("상세 주소"),
                                fieldWithPath("phone").description("전화번호"),
                                fieldWithPath("zipCode").description("우편번호")
                        ),
                        responseFields(
                                fieldWithPath("code").description("상태 코드"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("data").description("주문 번호")
                        )
                ));
    }
}
