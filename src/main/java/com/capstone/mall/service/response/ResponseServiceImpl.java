package com.capstone.mall.service.response;

import com.capstone.mall.model.ResponseDto;
import org.springframework.stereotype.Service;

@Service
public class ResponseServiceImpl implements ResponseService {

    public ResponseDto createResponseDto(int code, String message, Object data) {
        return ResponseDto.builder()
                .code(code)
                .message(message)
                .data(data)
                .build();
    }
}
