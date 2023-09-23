package com.capstone.mall.model.order;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Builder
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "orderDetail")
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_detail_id")
    private Long orderDetailId;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "seller_id")
    private String sellerId;

    private int price;

    private int quantity;

    private int result;

    @Column(name = "order_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @CreationTimestamp
    private Date orderDate;
}
