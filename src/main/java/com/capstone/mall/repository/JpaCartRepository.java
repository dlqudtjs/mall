package com.capstone.mall.repository;

import com.capstone.mall.model.cart.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaCartRepository extends JpaRepository<Cart, Long> {

    List<Cart> findByUserId(String userId);
}
