package com.capstone.mall.model.media;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class MediaRequestDto {

    private String email;
    private MultipartFile file;
}
