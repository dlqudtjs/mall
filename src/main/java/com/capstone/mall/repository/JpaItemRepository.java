package com.capstone.mall.repository;

import com.capstone.mall.model.item.Item;
import com.capstone.mall.model.item.ItemListProjectionInterface;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JpaItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByCategoryId(Long categoryId);

    @Query(value = "CALL GetItemsByCategoryId(:categoryId, :sortType)", nativeQuery = true)
    List<ItemListProjectionInterface> callGetItemsByCategoryId(Long categoryId, String sortType);
}