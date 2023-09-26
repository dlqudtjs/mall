package com.capstone.mall.repository;

import com.capstone.mall.model.item.Item;
import com.capstone.mall.model.item.ItemListProjectionInterface;
import com.capstone.mall.model.item.ItemProjectionInterface;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface JpaItemRepository extends JpaRepository<Item, Long> {

    String itemListView = "SELECT * FROM item_list_view ";

    String searchKeyword = "(SELECT item_id\n" +
            "from itemKeyword\n" +
            "where keyword_id in\n" +
            "(SELECT keyword_id \n" +
            "FROM mall.item_list_by_keyword_view\n" +
            "where keyword like %:keyword%))";


    // 카테고리 별 아이템 리스트 조회
    @Query(value = itemListView + "where categoryId = :categoryId", nativeQuery = true)
    List<ItemListProjectionInterface> itemListByCategoryId(Long categoryId);


    // 검색 키워드 별 아이템 리스트 조회
    @Query(value = itemListView + "where itemId in" + searchKeyword, nativeQuery = true)
    List<ItemListProjectionInterface> itemListByKeyword(String keyword);

    @Query(value = "CALL GetItemInfoByItemId(:itemId)", nativeQuery = true)
    Optional<ItemProjectionInterface> callGetItemInfoByItemId(Long itemId);

    @Query(value = "CALL GetItemsBySellerId(:sellerId)", nativeQuery = true)
    Optional<List<ItemListProjectionInterface>> callGetItemsBySellerId(String sellerId);

}