package com.capstone.mall.repository;

import com.capstone.mall.model.item.Item;
import com.capstone.mall.model.item.ItemProjectionInterface;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JpaItemRepository extends JpaRepository<Item, Long> {

    String itemListView = "SELECT * FROM item_detail_view ";

    String searchKeyword = "(SELECT item_id\n" +
            "from itemKeyword\n" +
            "where keyword_id in\n" +
            "(SELECT keyword_id \n" +
            "FROM keyword_id_by_keyword_view\n" +
            "where keyword like %:keyword%))";


    // 카테고리 별 아이템 리스트 조회
    @Query(value = itemListView + "where categoryId = :categoryId", nativeQuery = true)
    List<ItemProjectionInterface> getItemListByCategoryId(Long categoryId);


    // 검색 키워드 별 아이템 리스트 조회
    @Query(value = itemListView + "where itemId in" + searchKeyword, nativeQuery = true)
    List<ItemProjectionInterface> getItemListByKeyword(String keyword);

    @Query(value = itemListView + "where ItemId = :itemId", nativeQuery = true)
    ItemProjectionInterface getItemDetailByItemId(Long itemId);

    @Query(value = itemListView + "where sellerId = :sellerId", nativeQuery = true)
    List<ItemProjectionInterface> getItemsListBySellerId(String sellerId);
}