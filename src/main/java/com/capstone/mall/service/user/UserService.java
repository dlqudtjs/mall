package com.capstone.mall.service.user;

import com.capstone.mall.model.ResponseDto;
import com.capstone.mall.model.user.UserLoginRequestDto;
import com.capstone.mall.model.user.UserUpdateRequestDto;
import jakarta.servlet.http.HttpServletRequest;

public interface UserService {

    ResponseDto login(String userId, UserLoginRequestDto loginRequestDto) throws Exception;

    ResponseDto refresh(HttpServletRequest request);

    ResponseDto update(String userId, UserUpdateRequestDto userRequestDto);

    ResponseDto readAllUserList();
}
