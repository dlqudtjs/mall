package com.capstone.mall.service.cart;

import com.capstone.mall.model.ResponseDto;
import com.capstone.mall.model.cart.CartRequestDto;

public interface CartService {

    ResponseDto addCart(String userId, CartRequestDto cartRequestDto);

    ResponseDto readCartList(String userId, String token);

    ResponseDto deleteCart(Long cartId, String token);

    ResponseDto updateCart(Long cartId, CartRequestDto cartRequestDto, String token);
}
