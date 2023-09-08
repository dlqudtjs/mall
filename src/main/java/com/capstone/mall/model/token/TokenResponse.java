package com.capstone.mall.model.token;

import lombok.Builder;

@Builder
public class TokenResponse {

    private String accessToken;
    private String refreshToken;
}
