package com.capstone.mall.controller;

import com.capstone.mall.model.ResponseDto;
import com.capstone.mall.model.payment.PaymentRequestDto;
import com.capstone.mall.service.payment.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/users/payments/{userId}")
    public ResponseEntity<ResponseDto> payment(@PathVariable String userId,
                                               @RequestParam String items,
                                               @RequestBody PaymentRequestDto paymentRequestDto) {

        ResponseDto responseDto = paymentService.payment(userId, items, paymentRequestDto);

        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }
}
