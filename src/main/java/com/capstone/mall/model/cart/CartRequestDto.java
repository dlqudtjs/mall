package com.capstone.mall.model.cart;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CartRequestDto {
    private Long itemId;
    private int quantity;
}
