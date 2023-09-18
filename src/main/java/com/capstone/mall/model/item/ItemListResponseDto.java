package com.capstone.mall.model.item;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
// 아이템 리스트 조회 시 응답으로 보내는 DTO
public class ItemListResponseDto {
    List<ItemListProjection> items;

    int totalPage;
}
