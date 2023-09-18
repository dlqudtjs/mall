package com.capstone.mall.repository;

import com.capstone.mall.model.item.Item;
import com.capstone.mall.model.item.ItemListProjectionInterface;
import com.capstone.mall.model.item.ItemProjectionInterface;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface JpaItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByCategoryId(Long categoryId);

    @Query(value = "CALL GetItemsByCategoryId(:categoryId, :sortType)", nativeQuery = true)
    Optional<List<ItemListProjectionInterface>> callGetItemsByCategoryId(Long categoryId, String sortType);


    @Query(value = "CALL SearchItemsByKeyword(:search, :sortType)", nativeQuery = true)
    List<ItemListProjectionInterface> callGetItemsBySearch(String search, String sortType);

    @Query(value = "CALL CalculateAverageRating(:itemId)", nativeQuery = true)
    Optional<Double> callCalculateAverageRating(Long itemId);

    @Query(value = "CALL GetItemReviewCount(:itemId)", nativeQuery = true)
    int callGetItemReviewCount(Long itemId);

    @Query(value = "CALL GetItemInfoByItemId(:itemId)", nativeQuery = true)
    Optional<ItemProjectionInterface> callGetItemInfoByItemId(Long itemId);

    @Query(value = "CALL GetItemsBySellerId(:sellerId)", nativeQuery = true)
    Optional<List<ItemListProjectionInterface>> callGetItemsBySellerId(Long sellerId);

}