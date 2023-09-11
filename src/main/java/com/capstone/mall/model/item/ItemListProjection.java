package com.capstone.mall.model.item;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemListProjection {

    Long itemId;

    String name;

    String image1;

    String content;

    int price;

    int stock;

    int reviewCount;

    int rate;
}
