package com.capstone.mall.model.order;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Builder
@Getter
@Setter
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
