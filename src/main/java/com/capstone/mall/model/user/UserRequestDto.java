package com.capstone.mall.model.user;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class UserRequestDto {

    private String checkId;

    private Role role;
}
