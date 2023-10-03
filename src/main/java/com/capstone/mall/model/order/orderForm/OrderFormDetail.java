package com.capstone.mall.model.order.orderForm;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class OrderFormDetail {

    private Long itemId;

    private String itemName;

    private String image;

    private int quantity;

    private int price;
}