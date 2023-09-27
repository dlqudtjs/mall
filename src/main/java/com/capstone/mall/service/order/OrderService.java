package com.capstone.mall.service.order;

import com.capstone.mall.model.ResponseDto;
import com.capstone.mall.model.order.OrderUpdateRequestDto;

public interface OrderService {

    ResponseDto createOrderForm(String userId, String items);

    ResponseDto updateOrder(Long orderDetailId, OrderUpdateRequestDto orderUpdateRequestDto, String token);

    ResponseDto getSoldOrderList(String userId, int pageNum, int pageSize, String token);

    ResponseDto getPurchaseList(String userId, int pageNum, int pageSize, String token);
}
