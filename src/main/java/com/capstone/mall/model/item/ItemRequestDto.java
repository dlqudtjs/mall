package com.capstone.mall.model.item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@Getter
// 아이템 생성 시 요청으로 받는 DTO
public class ItemRequestDto {

    private String sellerId;
    private String name;
    private Long categoryId;
    private int price;
    private int stock;
    private String content;
    private String image1;
    private String image2;
    private String image3;

    private List<String> keywords;
}
