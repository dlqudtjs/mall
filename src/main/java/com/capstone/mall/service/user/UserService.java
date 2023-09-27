package com.capstone.mall.service.user;

import com.capstone.mall.model.ResponseDto;
import com.capstone.mall.model.user.UserRequestDto;
import jakarta.servlet.http.HttpServletRequest;

public interface UserService {

    ResponseDto login(String userId, UserRequestDto userRequestDto) throws Exception;

    ResponseDto refresh(HttpServletRequest request);

    ResponseDto update(String userId, UserRequestDto userRequestDto);

    ResponseDto readAllUserList();
}
