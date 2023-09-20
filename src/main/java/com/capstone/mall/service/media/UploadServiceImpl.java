package com.capstone.mall.service.media;

import com.capstone.mall.model.ResponseDto;
import com.capstone.mall.model.media.MediaRequestDto;
import com.capstone.mall.service.response.ResponseService;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UploadServiceImpl implements UploadService {

    @Value("${gcp.bucket.name}")
    private String bucketName;

    @Value("${gcp.bucket.image.name}")
    private String imagePath;

    private final ResponseService responseService;
    private final Storage storage;

    @Override
    public ResponseDto imageUpload(MediaRequestDto mediaRequestDto) throws IOException {
        String uuid = UUID.randomUUID().toString();
        String extension = mediaRequestDto.getFile().getContentType();

        String objectName = imagePath + mediaRequestDto.getEmail() + uuid;

        BlobInfo blobInfo = storage.create(
                BlobInfo.newBuilder(bucketName, objectName)
                        .setContentType(extension)
                        .build(),
                mediaRequestDto.getFile().getBytes());

        return responseService.createResponseDto(200, "", blobInfo.getMediaLink());
    }
}