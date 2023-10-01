package com.capstone.mall.docs.user;

import com.capstone.mall.controller.UserController;
import com.capstone.mall.docs.RestDocumentSupport;
import com.capstone.mall.model.ResponseDto;
import com.capstone.mall.model.token.TokenResponse;
import com.capstone.mall.model.user.UserLoginRequestDto;
import com.capstone.mall.model.user.UserResponseDto;
import com.capstone.mall.model.user.UserUpdateRequestDto;
import com.capstone.mall.service.user.UserService;
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

@WebMvcTest(controllers = UserController.class)
public class UserApiDocsTest extends RestDocumentSupport {

    @MockBean
    private UserService userService;

    @Test
    void login() throws Exception {
        TokenResponse tokenResponse = TokenResponse.builder()
                .accessToken("testAccessToken")
                .refreshToken("testRefreshToken")
                .userId("testId")
                .build();

        // given
        UserLoginRequestDto userLoginRequestDto = new UserLoginRequestDto();
        userLoginRequestDto.setCheckId("testCheckId");

        given(userService.login(any(), any())).willReturn(
                ResponseDto.builder()
                        .code(HttpStatus.OK.value())
                        .message("")
                        .data(tokenResponse)
                        .build());

        // when & then
        mockMvc.perform(post("/api/public/login/{userId}", "testId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userLoginRequestDto)))
                .andExpect(status().isOk())
                .andDo(document("{class-name}/{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("checkId").description("암호화된 userId")
                        ),
                        responseFields(
                                fieldWithPath("code").description("상태 코드"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("data.accessToken").description("Access Token"),
                                fieldWithPath("data.refreshToken").description("Refresh Token"),
                                fieldWithPath("data.userId").description("유저 ID")
                        )
                ));
    }

    @Test
    void refresh() throws Exception {
        TokenResponse tokenResponse = TokenResponse.builder()
                .accessToken("testAccessToken")
                .refreshToken("testRefreshToken")
                .userId("testId")
                .build();

        // given
        given(userService.refresh(any())).willReturn(
                ResponseDto.builder()
                        .code(HttpStatus.OK.value())
                        .message("")
                        .data(tokenResponse)
                        .build());

        // when & then
        mockMvc.perform(get("/api/users/refresh")
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
                                fieldWithPath("data.accessToken").description("Access Token"),
                                fieldWithPath("data.refreshToken").description("Refresh Token"),
                                fieldWithPath("data.userId").description("유저 ID")
                        )
                ));
    }

    @Test
    void update() throws Exception {
        UserUpdateRequestDto userUpdateRequestDto = new UserUpdateRequestDto();
        userUpdateRequestDto.setRole("ROLE_USER");

        // given
        given(userService.update(any(), any())).willReturn(
                ResponseDto.builder()
                        .code(HttpStatus.OK.value())
                        .message("")
                        .data("testId")
                        .build());

        // when & then
        mockMvc.perform(patch("/api/admin/users/{userId}", "testId")
                        .header("Authorization", "Bearer " + "token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdateRequestDto)))
                .andExpect(status().isOk())
                .andDo(document("{class-name}/{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("JWT 토큰")
                        ),
                        requestFields(
                                fieldWithPath("role").description("유저 권한")
                        ),
                        responseFields(
                                fieldWithPath("code").description("상태코드"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("data").description("유저 ID")
                        )
                ));
    }

    @Test
    void readAllUserList() throws Exception {
        // given
        List<UserResponseDto> users = List.of(
                UserResponseDto.builder()
                        .userId("testId")
                        .metaId("testMetaId")
                        .role("ROLE_USER")
                        .build(),
                UserResponseDto.builder()
                        .userId("testId2")
                        .metaId("testMetaId2")
                        .role("ROLE_SELLER")
                        .build()
        );

        given(userService.readAllUserList()).willReturn(
                ResponseDto.builder()
                        .code(HttpStatus.OK.value())
                        .message("")
                        .data(users)
                        .build());

        // when & then
        mockMvc.perform(get("/api/admin/users")
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
                                fieldWithPath("data[].userId").description("식별 유저ID"),
                                fieldWithPath("data[].metaId").description("메타마스크 ID"),
                                fieldWithPath("data[].role").description("유저 권한")
                        )
                ));
    }
}
