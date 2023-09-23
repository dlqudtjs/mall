package com.capstone.mall.model.payment;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class PaymentRequestDto {

    String recipient;
    String address;
    String detailAddress;
    String phone;
    int zipCode;
}
