package com.capstone.mall.service.payment;

import com.capstone.mall.model.ResponseDto;

public interface PaymentService {

    ResponseDto createPayment(String userId, Long itemId, int quantity);

}
