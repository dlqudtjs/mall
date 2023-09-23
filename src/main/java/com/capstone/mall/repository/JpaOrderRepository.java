package com.capstone.mall.repository;

import com.capstone.mall.model.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaOrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserId(String userId);
}
