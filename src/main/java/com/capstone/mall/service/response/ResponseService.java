package com.capstone.mall.service.response;

import com.capstone.mall.model.ResponseDto;

public interface ResponseService {

    ResponseDto createResponseDto(int code, String message, Object data);
}
