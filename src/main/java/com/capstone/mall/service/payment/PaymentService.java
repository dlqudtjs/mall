package com.capstone.mall.service.payment;

import com.capstone.mall.model.ResponseDto;
import com.capstone.mall.model.payment.PaymentRequestDto;

public interface PaymentService {

    ResponseDto payment(String userId, String items, PaymentRequestDto paymentRequestDto);
}
