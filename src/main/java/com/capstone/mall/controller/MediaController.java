package com.capstone.mall.controller;

import com.capstone.mall.model.ResponseDto;
import com.capstone.mall.model.media.ImageUploadRequestDto;
import com.capstone.mall.service.media.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MediaController {

    private final MediaService mediaService;

    @PostMapping("/users/uploads/images")
    public ResponseEntity<ResponseDto> uploadImage(ImageUploadRequestDto imageUploadRequestDto) throws IOException {
        ResponseDto responseDto = mediaService.imageUpload(imageUploadRequestDto);

        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }
}
