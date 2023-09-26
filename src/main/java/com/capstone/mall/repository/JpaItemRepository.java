package com.capstone.mall.repository;

import com.capstone.mall.model.item.Item;
import com.capstone.mall.model.item.ItemListProjectionInterface;
import com.capstone.mall.model.item.ItemProjectionInterface;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface JpaItemRepository extends JpaRepository<Item, Long> {

    String itemListByCategoryId = "SELECT * FROM item_list_by_category_view ";


    // 카테고리 정렬
    @Query(value = itemListByCategoryId + "where categoryId = :categoryId", nativeQuery = true)
    List<ItemListProjectionInterface> itemListByCategoryId(Long categoryId);


    @Query(value = "CALL SearchItemsByKeyword(:search, :sortType)", nativeQuery = true)
    List<ItemListProjectionInterface> callGetItemsBySearch(String search, String sortType);

    @Query(value = "CALL GetItemInfoByItemId(:itemId)", nativeQuery = true)
    Optional<ItemProjectionInterface> callGetItemInfoByItemId(Long itemId);

    @Query(value = "CALL GetItemsBySellerId(:sellerId)", nativeQuery = true)
    Optional<List<ItemListProjectionInterface>> callGetItemsBySellerId(String sellerId);

}