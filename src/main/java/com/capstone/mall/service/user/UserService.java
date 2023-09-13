package com.capstone.mall.service.user;

import com.capstone.mall.model.ResponseDto;
import com.capstone.mall.model.user.UserRequestDto;

public interface UserService {

    ResponseDto login(String userId, UserRequestDto userRequestDto) throws Exception;

    ResponseDto refresh(String refreshToken);

    ResponseDto update(String userId, UserRequestDto userRequestDto);
}
