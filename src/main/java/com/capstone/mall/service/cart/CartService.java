package com.capstone.mall.service.cart;

import com.capstone.mall.model.ResponseDto;
import com.capstone.mall.model.cart.CartAddRequestDto;
import com.capstone.mall.model.cart.CartUpdateRequestDto;

public interface CartService {

    ResponseDto addCart(String userId, CartAddRequestDto cartRequestDto);

    ResponseDto readCartList(String userId, String token);

    ResponseDto deleteCart(Long cartId, String token);

    ResponseDto updateCart(Long cartId, CartUpdateRequestDto CartUpdateRequestDto, String token);
}
