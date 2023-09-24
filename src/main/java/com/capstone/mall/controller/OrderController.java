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

    // 주문서 폼 생성
    @GetMapping("/users/form/orders/{userId}")
    public ResponseEntity<ResponseDto> createOrderForm(@PathVariable String userId, @RequestParam String items) {
        ResponseDto responseDto = orderService.createOrderForm(userId, items);

        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    // 주문 상태 업데이트
    @PatchMapping("/sellers/orders/{orderDetailId}")
    public ResponseEntity<ResponseDto> updateOrder(@PathVariable Long orderDetailId,
                                                   @RequestBody OrderRequestDto orderRequestDto,
                                                   @RequestHeader("Authorization") String token) {
        ResponseDto responseDto = orderService.updateOrder(orderDetailId, orderRequestDto, token);

        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    // 판매한 주문 조회
    @GetMapping("/sellers/orders/{userId}")
    public ResponseEntity<ResponseDto> getOrderList(@PathVariable String userId,
                                                    @RequestParam(required = false, defaultValue = "1") int pageNum,
                                                    @RequestParam(required = false, defaultValue = "10") int pageSize,
                                                    @RequestHeader("Authorization") String token) {
        ResponseDto responseDto = orderService.getSoldOrderList(userId, pageNum, pageSize, token);

        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    // 구매한 주문 조회
    @GetMapping("/users/orders/{userId}")
    public ResponseEntity<ResponseDto> getPurchaseList(@PathVariable String userId,
                                                       @RequestParam(required = false, defaultValue = "1") int pageNum,
                                                       @RequestParam(required = false, defaultValue = "10") int pageSize,
                                                       @RequestHeader("Authorization") String token) {
        ResponseDto responseDto = orderService.getPurchaseList(userId, pageNum, pageSize, token);

        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }
}
