package com.capstone.mall.model.cart;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CartUpdateRequestDto {

    private int quantity;

    public CartUpdateRequestDto() {
    }
}
