package com.capstone.mall.model.order.orderForm;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@Builder
@Getter
public class OrderFormResponseDto {

    private List<OrderFormDetail> orders;
}
