package com.capstone.mall.service.item;

import com.capstone.mall.model.ResponseDto;
import com.capstone.mall.model.item.ItemRequestDto;

public interface ItemService {

    // 검색으로 아이템 리스트 조회
    ResponseDto readItemList(String search, int pageNum, int pageSize, String sort, String sortType);

    // 카테고리로 아이템 리스트 조회
    ResponseDto readItemList(Long categoryId, int pageNum, int pageSize, String sort, String sortType);

    // 판매중인 상품 목록 조회 (판매자 전용)
    ResponseDto readItemListBySellerId(String sellerId, int pageNum, int pageSize, String token);

    ResponseDto createItem(String sellerId, ItemRequestDto itemRequestDto);

    ResponseDto readItem(Long itemId);

    ResponseDto updateItem(Long itemId, ItemRequestDto itemRequestDto, String token);

    ResponseDto deleteItem(Long itemId, String token);
}
