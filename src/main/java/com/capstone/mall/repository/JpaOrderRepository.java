package com.capstone.mall.repository;

import com.capstone.mall.model.order.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaOrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findAllByUserId(String userId, Pageable pageable);
}
