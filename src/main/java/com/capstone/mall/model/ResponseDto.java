package com.capstone.mall.model;

import lombok.Builder;

@Builder
@Getters
public class ResponseDto {

    private int code;
    private String message;
    private Object data;
}
