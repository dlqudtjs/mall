package com.capstone.mall.model.payment;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class PaymentRequestDto {

    String recipient;
    String address;
    String detailAddress;
    String phone;
    int zipCode;
}
