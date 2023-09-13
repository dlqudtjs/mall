package com.capstone.mall.model.cart;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class CartResponseDto {

    private Long cartId;
    private Long itemId;
    private String name;
    private int quantity;
    private int price;
    private String image1;
    private int stock;
}
