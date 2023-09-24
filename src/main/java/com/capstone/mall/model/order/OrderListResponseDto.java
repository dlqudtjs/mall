package com.capstone.mall.model.order;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
// Order 리스트 조회 시 응답으로 보내는 DTO
public class OrderListResponseDto {

    private List<OrderResponseDto> orders;
    private int totalPage;
}
