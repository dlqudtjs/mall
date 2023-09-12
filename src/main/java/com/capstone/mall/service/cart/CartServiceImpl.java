package com.capstone.mall.service.cart;

import com.capstone.mall.model.ResponseDto;
import com.capstone.mall.model.cart.Cart;
import com.capstone.mall.model.cart.CartRequestDto;
import com.capstone.mall.repository.JpaCartRepository;
import com.capstone.mall.service.response.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {

    private final JpaCartRepository cartRepository;
    private final ResponseService responseService;

    @Override
    public ResponseDto addCart(CartRequestDto cartRequestDto) {
        Cart cart = Cart.builder()
                .userId(cartRequestDto.getUserId())
                .itemId(cartRequestDto.getItemId())
                .quantity(cartRequestDto.getQuantity())
                .build();

        Cart savedCart = cartRepository.save(cart);

        return responseService.createResponseDto(200, "", savedCart.getCartId());
    }

    @Override
    public ResponseDto readCartList(String userId) {
        List<Cart> cartList = cartRepository.findByUserId(userId);

        return responseService.createResponseDto(200, "", cartList);
    }

    @Override
    public ResponseDto deleteCart(Long cartId) {
        if (!cartRepository.existsById(cartId)) {
            return responseService.createResponseDto(200, "cart does not exist", null);
        }

        cartRepository.deleteById(cartId);

        return responseService.createResponseDto(200, "", cartId);
    }

    @Override
    public ResponseDto updateCart(Long cartId, CartRequestDto cartRequestDto) {
        Cart cart = cartRepository.findById(cartId).orElse(null);

        if (cart == null) {
            return responseService.createResponseDto(200, "Not Found Cart", null);
        }

        cart.setQuantity(cartRequestDto.getQuantity());

        return responseService.createResponseDto(200, "", cartId);
    }
}
