package com.capstone.mall.service.media;

import com.capstone.mall.model.ResponseDto;
import com.capstone.mall.model.media.ImageUploadRequestDto;

import java.io.IOException;

public interface MediaService {

    ResponseDto imageUpload(ImageUploadRequestDto imageUploadRequestDto) throws IOException;
}
