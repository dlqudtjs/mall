package com.capstone.mall.model.item;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class ItemResponseDto {

    List<Item> items;

    int totalPage;
}
