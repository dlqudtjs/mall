package com.capstone.mall.repository;

import com.capstone.mall.model.order.OrderDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaOrderDetailRepository extends JpaRepository<OrderDetail, Long> {

    Page<OrderDetail> findAllBySellerId(String sellerId, Pageable pageable);

    List<OrderDetail> findAllByOrderId(Long orderId);
}
