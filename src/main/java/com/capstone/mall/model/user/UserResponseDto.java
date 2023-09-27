package com.capstone.mall.model.user;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserResponseDto {

    private String userId;

    private String metaId;

    private String role;
}
