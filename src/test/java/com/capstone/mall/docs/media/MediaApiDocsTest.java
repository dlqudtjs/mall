package com.capstone.mall.docs.media;

import com.capstone.mall.controller.MediaController;
import com.capstone.mall.docs.RestDocumentSupport;
import com.capstone.mall.model.ResponseDto;
import com.capstone.mall.model.media.ImageUploadRequestDto;
import com.capstone.mall.service.media.MediaService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MediaController.class)
public class MediaApiDocsTest extends RestDocumentSupport {

    @MockBean
    private MediaService mediaService;

    @Test
    void uploadImage() throws Exception {
        ImageUploadRequestDto imageUploadRequestDto = new ImageUploadRequestDto();
        imageUploadRequestDto.setFile(null);

        // given
        given(mediaService.imageUpload(any())).willReturn(
                ResponseDto.builder()
                        .code(HttpStatus.OK.value())
                        .message("")
                        .data("testImageUrl")
                        .build());

        // when & then
        mockMvc.perform(post("/api/users/uploads/images")
                        .contentType("multipart/form-data")
                        .header("Authorization", "Bearer " + "token")
                        .content(objectMapper.writeValueAsString(imageUploadRequestDto)))
                .andExpect(status().isOk())
                .andDo(document("{class-name}/{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer 토큰")
                        ),
                        requestFields(
                                fieldWithPath("file").description("이미지 파일").type("multipart/form-data")
                        ),
                        responseFields(
                                fieldWithPath("code").description("상태 코드"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("data").description("이미지 url")
                        )
                ));
    }
}
