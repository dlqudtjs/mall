package com.capstone.mall.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;

import java.io.IOException;

public class CustomAccessDeniedHandler implements org.springframework.security.web.access.AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // 인가 처리 과정에서 예외가 발생했을 때, 403 Forbidden 에러를 리턴
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
    }
}
