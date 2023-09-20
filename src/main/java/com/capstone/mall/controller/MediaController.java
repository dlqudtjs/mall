package com.capstone.mall.controller;

import com.capstone.mall.model.ResponseDto;
import com.capstone.mall.model.media.MediaRequestDto;
import com.capstone.mall.service.media.UploadService;
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

    private final UploadService uploadService;

    @PostMapping("/users/uploads/images")
    public ResponseEntity<ResponseDto> uploadImage(MediaRequestDto mediaRequestDto) throws IOException {
        ResponseDto responseDto = uploadService.imageUpload(mediaRequestDto);

        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }
}
