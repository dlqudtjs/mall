package com.capstone.mall.model.cart;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CartAddRequestDto {
    private Long itemId;
    private int quantity;
}
