package com.capstone.mall.model;

import lombok.Builder;

@Builder
public class ResponseDto {

    private int code;
    private String message;
    private Object data;
}
