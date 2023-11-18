package com.capstone.mall.service.cart;

import com.capstone.mall.model.ResponseDto;
import com.capstone.mall.model.cart.Cart;
import com.capstone.mall.model.cart.CartAddRequestDto;
import com.capstone.mall.model.cart.CartResponseDto;
import com.capstone.mall.model.cart.CartUpdateRequestDto;
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
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {

    private final JpaCartRepository cartRepository;
    private final ResponseService responseService;
    private final JpaItemRepository itemRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public ResponseDto addCart(String userId, CartAddRequestDto cartRequestDto) {
        Cart existCart = getExistCart(userId, cartRequestDto.getItemId());

        if (existCart != null) {
            existCart.setQuantity(cartRequestDto.getQuantity());
            return responseService.createResponseDto(200, "", existCart.getCartId());
        }

        Cart cart = Cart.builder()
                .userId(userId)
                .itemId(cartRequestDto.getItemId())
                .quantity(cartRequestDto.getQuantity())
                .build();

        Cart savedCart = cartRepository.save(cart);

        return responseService.createResponseDto(200, "", savedCart.getCartId());
    }

    @Override
    public ResponseDto readCartList(String userId, String token) {
        List<Cart> cartList = cartRepository.findByUserId(userId);

        if (!jwtTokenProvider.getUserIdByBearerToken(token).equals(userId)) {
            return responseService.createResponseDto(403, "token does not match", null);
        }

        List<CartResponseDto> carts = new ArrayList<>();
        for (Cart cart : cartList) {
            Optional<Item> item = itemRepository.findById(cart.getItemId());

            if (item.isEmpty()) {
                continue;
            }

            CartResponseDto cartResponseDto = CartResponseDto.builder()
                    .cartId(cart.getCartId())
                    .itemId(cart.getItemId())
                    .name(item.get().getName())
                    .quantity(cart.getQuantity())
                    .price(item.get().getPrice())
                    .image1(item.get().getImage1())
                    .stock(item.get().getStock())
                    .build();

            carts.add(cartResponseDto);
        }

        return responseService.createResponseDto(200, "", carts);
    }

    @Override
    public ResponseDto updateCart(Long cartId, CartUpdateRequestDto cartUpdateRequestDto, String token) {
        Optional<Cart> cart = cartRepository.findById(cartId);

        if (cart.isEmpty()) {
            return responseService.createResponseDto(200, "cart does not exist", null);
        }

        if (!jwtTokenProvider.getUserIdByBearerToken(token).equals(cart.get().getUserId())) {
            return responseService.createResponseDto(403, "token does not match", null);
        }

        cart.get().setQuantity(cartUpdateRequestDto.getQuantity());

        return responseService.createResponseDto(200, "", cartId);
    }

    @Override
    public ResponseDto deleteCart(Long cartId, String token) {
        Optional<Cart> cart = cartRepository.findById(cartId);

        if (cart.isEmpty()) {
            return responseService.createResponseDto(200, "cart does not exist", null);
        }

        if (!jwtTokenProvider.getUserIdByBearerToken(token).equals(cart.get().getUserId())) {
            return responseService.createResponseDto(403, "token does not match", null);
        }

        cartRepository.deleteById(cartId);

        return responseService.createResponseDto(200, "", cartId);
    }

    private Cart getExistCart(String userId, Long itemId) {
        return cartRepository.findByUserId(userId).stream()
                .filter(cart -> cart.getItemId().equals(itemId))
                .findFirst()
                .orElse(null);
    }
}
