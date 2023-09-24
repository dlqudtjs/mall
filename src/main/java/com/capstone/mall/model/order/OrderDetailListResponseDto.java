package com.capstone.mall.model.order;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
// OrderDetail 리스트 조회 시 응답으로 보내는 DTO
public class OrderDetailListResponseDto {

    List<OrderDetailResponseDto> orders;

    private int totalPage;
}
