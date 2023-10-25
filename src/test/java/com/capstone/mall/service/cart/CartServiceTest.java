package com.capstone.mall.service.cart;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CartServiceTest {

    @Autowired
    private CartService cartService;

    @Test
    public void test() {
        System.out.println(cartService);
    }
}
