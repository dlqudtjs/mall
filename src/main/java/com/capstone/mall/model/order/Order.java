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
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "total_count")
    private int totalCount;

    @Column(name = "total_price")
    private int totalPrice;

    private String recipient;

    private String address;

    @Column(name = "detail_address")
    private String detailAddress;

    private String phone;

    @Column(name = "zip_code")
    private int zipCode;

    @Column(name = "order_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @CreationTimestamp
    private Date orderDate;
}
