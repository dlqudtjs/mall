package com.capstone.mall.service.cart;

import com.capstone.mall.model.ResponseDto;
import com.capstone.mall.model.cart.Cart;
import com.capstone.mall.model.cart.CartRequestDto;
import com.capstone.mall.model.cart.CartResponseDto;
import com.capstone.mall.model.item.Item;
import com.capstone.mall.repository.JpaCartRepository;
import com.capstone.mall.repository.JpaItemRepository;
import com.capstone.mall.security.JwtTokenProvider;
import com.capstone.mall.service.response.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {

    private final JpaCartRepository cartRepository;
    private final ResponseService responseService;
    private final JpaItemRepository itemRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public ResponseDto addCart(String userId, CartRequestDto cartRequestDto) {
        Cart cart = Cart.builder()
                .userId(userId)
                .itemId(cartRequestDto.getItemId())
                .quantity(cartRequestDto.getQuantity())
                .build();

        Cart savedCart = cartRepository.save(cart);

        return responseService.createResponseDto(200, "", savedCart.getCartId());
    }

    @Override
    public ResponseDto readCartList(String userId) {
        List<Cart> cartList = cartRepository.findByUserId(userId);

        List<CartResponseDto> carts = new ArrayList<>();

        for (Cart cart : cartList) {
            System.out.println(cart.getCartId());
            Item item = itemRepository.findById(cart.getItemId()).orElse(null);

            if (item == null) {
                return responseService.createResponseDto(200, "deleted items in cart ", null);
            }

            CartResponseDto cartResponseDto = CartResponseDto.builder()
                    .cartId(cart.getCartId())
                    .itemId(cart.getItemId())
                    .name(item.getName())
                    .quantity(cart.getQuantity())
                    .price(item.getPrice())
                    .image1(item.getImage1())
                    .stock(item.getStock())
                    .build();

            carts.add(cartResponseDto);
        }

        return responseService.createResponseDto(200, "", carts);
    }

    @Override
    public ResponseDto deleteCart(Long cartId, String token) {
        Cart cart = cartRepository.findById(cartId).orElse(null);

        if (cart == null) {
            return responseService.createResponseDto(200, "cart does not exist", null);
        }

        if (jwtTokenProvider.getUserId(token).equals(cart.getUserId())) {
            return responseService.createResponseDto(400, "token does not match", null);
        }

        cartRepository.deleteById(cartId);

        return responseService.createResponseDto(200, "", cartId);
    }

    @Override
    public ResponseDto updateCart(Long cartId, CartRequestDto cartRequestDto, String token) {
        Cart cart = cartRepository.findById(cartId).orElse(null);

        if (cart == null) {
            return responseService.createResponseDto(200, "Not Found Cart", null);
        }

        if (jwtTokenProvider.getUserId(token).equals(cart.getUserId())) {
            return responseService.createResponseDto(400, "token does not match", null);
        }

        cart.setQuantity(cartRequestDto.getQuantity());

        return responseService.createResponseDto(200, "", cartId);
    }
}
