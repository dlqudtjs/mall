package com.capstone.mall.exception;

import com.capstone.mall.model.ResponseDto;

public class Exception {

    public static ResponseDto throwException(int code, String message) {
        return ResponseDto
                .builder()
                .code(code)
                .message(message)
                .data(null)
                .build();
    }
}
