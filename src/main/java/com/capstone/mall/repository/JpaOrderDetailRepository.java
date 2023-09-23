package com.capstone.mall.repository;

import com.capstone.mall.model.order.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaOrderDetailRepository extends JpaRepository<OrderDetail, Long> {
}
