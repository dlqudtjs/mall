package com.capstone.mall.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ResponseDto {

    private int code;
    private String message;
    private Object data;
}
