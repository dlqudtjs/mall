package com.capstone.mall.service.order;

import com.capstone.mall.model.ResponseDto;

public interface OrderService {

    ResponseDto createOrderForm(String userId, String items);
}
