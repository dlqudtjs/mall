package com.capstone.mall.service.cart;

import com.capstone.mall.model.ResponseDto;
import com.capstone.mall.model.cart.CartRequestDto;

public interface CartService {

    ResponseDto addCart(CartRequestDto cartRequestDto);

    ResponseDto readCartList(String userId);

    ResponseDto deleteCart(Long cartId);

    ResponseDto updateCart(Long cartId, CartRequestDto cartRequestDto);
}
