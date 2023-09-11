package com.capstone.mall.service.item;

import com.capstone.mall.model.ResponseDto;
import com.capstone.mall.model.item.ItemRequestDto;

public interface ItemService {

    // 검색으로 아이템 리스트 조회
    ResponseDto readItemList(String search, int pageNum, int pageSize, String sortType);

    // 카테고리로 아이템 리스트 조회
    ResponseDto readItemList(Long categoryId, int pageNum, int pageSize, String sortType);

    ResponseDto createItem(ItemRequestDto itemRequestDto);
}
