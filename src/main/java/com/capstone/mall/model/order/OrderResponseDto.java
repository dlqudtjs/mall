package com.capstone.mall.model.order;

import com.capstone.mall.model.order.orderDetail.OrderDetailResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Builder
@Getter
@Setter
// 구매한 상품 목록을 보여주는 DTO
public class OrderResponseDto {
    private Long orderId;
    private Date orderDate;
    private int totalPrice;

    List<OrderDetailResponseDto> orderDetails;
}
