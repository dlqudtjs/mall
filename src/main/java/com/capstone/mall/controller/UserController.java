package com.capstone.mall.controller;

import com.capstone.mall.model.ResponseDto;
import com.capstone.mall.model.user.UserRequestDto;
import com.capstone.mall.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/login/{metaId}")
    public ResponseEntity<ResponseDto> login(@PathVariable String metaId, @RequestBody UserRequestDto userRequestDto) throws Exception {
        ResponseDto responseDto = userService.login(metaId, userRequestDto);

        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @GetMapping("/api/users/refresh")
    public ResponseEntity<ResponseDto> refresh(@RequestHeader("Authorization") String refreshToken) {
        ResponseDto responseDto = userService.refresh(refreshToken);

        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @PatchMapping("/api/admin/users/{userId}")
    public ResponseEntity<ResponseDto> update(@PathVariable String userId, @RequestBody UserRequestDto userRequestDto) {
        ResponseDto responseDto = userService.update(userId, userRequestDto);

        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

}
