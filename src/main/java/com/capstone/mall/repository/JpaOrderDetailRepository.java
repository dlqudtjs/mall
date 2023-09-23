package com.capstone.mall.repository;

import com.capstone.mall.model.order.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaOrderDetailRepository extends JpaRepository<OrderDetail, Long> {

    List<OrderDetail> findBySellerId(String sellerId);

    List<OrderDetail> findByOrderId(Long orderId);
}
