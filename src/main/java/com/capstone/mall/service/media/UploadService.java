package com.capstone.mall.service.media;

import com.capstone.mall.model.ResponseDto;
import com.capstone.mall.model.media.MediaRequestDto;

import java.io.IOException;

public interface UploadService {

    ResponseDto imageUpload(MediaRequestDto mediaRequestDto) throws IOException;
}
