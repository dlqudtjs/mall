package com.capstone.mall.model.order.orderDetail;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Builder
@Getter
@Setter
// order 안에 같이 조회되는 orderDetail 응답 DTO
public class OrderDetailResponseDto {

    private Long orderDetailId;
    private Long orderId;
    private Long itemId;
    private String itemName;
    private String sellerId;
    private String image;
    private int price;
    private int quantity;
    private int result;
    private Date orderDate;
}
