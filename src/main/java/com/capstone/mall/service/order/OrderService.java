package com.capstone.mall.service.order;

import com.capstone.mall.model.ResponseDto;
import com.capstone.mall.model.order.OrderRequestDto;

public interface OrderService {

    ResponseDto createOrderForm(String userId, String items);

    ResponseDto updateOrder(Long orderDetailId, OrderRequestDto orderRequestDto);

    ResponseDto getSoldOrderList(String userId);
}
