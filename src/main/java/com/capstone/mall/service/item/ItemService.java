package com.capstone.mall.service.item;

import com.capstone.mall.model.ResponseDto;
import com.capstone.mall.model.item.ItemCreateRequestDto;
import com.capstone.mall.model.item.ItemUpdateRequestDto;

public interface ItemService {

    ResponseDto readItem(Long itemId);

    // 검색으로 아이템 리스트 조회
    ResponseDto readItemListBySearch(String search, int pageNum, int pageSize, String sort, String sortType);

    // 카테고리로 아이템 리스트 조회
    ResponseDto readItemListByCategoryId(Long categoryId, int pageNum, int pageSize, String sort, String sortType);

    // 판매중인 상품 목록 조회 (판매자 전용)
    ResponseDto readItemListBySellerId(String sellerId, int pageNum, int pageSize, String token);

    ResponseDto createItem(String sellerId, ItemCreateRequestDto itemRequestDto);

    ResponseDto updateItem(Long itemId, ItemUpdateRequestDto itemRequestDto, String token);

    ResponseDto deleteItem(Long itemId, String token);
}
