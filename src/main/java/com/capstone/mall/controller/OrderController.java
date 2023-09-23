package com.capstone.mall.controller;

import com.capstone.mall.model.ResponseDto;
import com.capstone.mall.model.order.OrderRequestDto;
import com.capstone.mall.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/users/form/orders/{userId}")
    public ResponseEntity<ResponseDto> createOrderForm(@PathVariable String userId, @RequestParam String items) {
        ResponseDto responseDto = orderService.createOrderForm(userId, items);

        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @PatchMapping("/sellers/orders/{orderDetailId}")
    public ResponseEntity<ResponseDto> updateOrder(@PathVariable Long orderDetailId, @RequestBody OrderRequestDto orderRequestDto) {
        ResponseDto responseDto = orderService.updateOrder(orderDetailId, orderRequestDto);

        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @GetMapping("/sellers/orders/{userId}")
    public ResponseEntity<ResponseDto> getOrderList(@PathVariable String userId) {
        ResponseDto responseDto = orderService.getSoldOrderList(userId);

        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @GetMapping("/users/orders/{userId}")
    public ResponseEntity<ResponseDto> getPurchaseList(@PathVariable String userId) {
        ResponseDto responseDto = orderService.getPurchaseList(userId);

        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }
}
