package com.capstone.mall.service.item;

import com.capstone.mall.model.ResponseDto;
import com.capstone.mall.model.item.*;
import com.capstone.mall.model.itemKeyword.ItemKeyword;
import com.capstone.mall.model.itemKeyword.ItemKeywordID;
import com.capstone.mall.model.keyword.Keyword;
import com.capstone.mall.repository.JpaItemKeywordRepository;
import com.capstone.mall.repository.JpaItemRepository;
import com.capstone.mall.repository.JpaKeywordRepository;
import com.capstone.mall.service.response.ResponseServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemServiceImpl implements ItemService {

    private final JpaItemRepository itemRepository;
    private final ResponseServiceImpl responseService;
    private final JpaKeywordRepository keywordRepository;
    private final JpaItemKeywordRepository itemKeywordRepository;


    @Override
    // 검색으로 아이템 리스트 조회
    public ResponseDto readItemList(String search, int pageNum, int pageSize, String sortType) {


        return null;
    }

    @Override
    // 카테고리로 아이템 리스트 조회
    public ResponseDto readItemList(Long categoryId, int pageNum, int pageSize, String sortType) {
        List<ItemListProjectionInterface> items = itemRepository.callGetItemsByCategoryId(categoryId, sortType);

        List<ItemListProjection> itemList = new ArrayList<>();
        for (ItemListProjectionInterface item : items) {
            itemList.add(ItemListProjection.builder()
                    .itemId(item.getItemId())
                    .name(item.getName())
                    .image1(item.getImage1())
                    .price(item.getPrice())
                    .content(item.getContent())
                    .rate(item.getItemAvgReview())
                    .reviewCount(item.getItemReviewCount())
                    .stock(item.getStock())
                    .build());
        }
        // 정렬 (지금은 데이터베이스에서 정렬을 함)
//        items = sortItemList(items, sortType);

        // 총 페이지 수
        int totalPage = (int) Math.ceil((double) itemList.size() / pageSize);

        // 페이지네이션
        itemList = pagination(itemList, pageNum, pageSize);

        ItemResponseDto itemResponseDto = ItemResponseDto.builder()
                .items(itemList)
                .totalPage(totalPage)
                .build();

        return responseService.createResponseDto(200, "", itemResponseDto);
    }

    @Override
    public ResponseDto createItem(ItemRequestDto itemRequestDto) {
        Item item = Item.builder()
                .sellerId(itemRequestDto.getSellerId())
                .name(itemRequestDto.getName())
                .categoryId(itemRequestDto.getCategoryId())
                .price(itemRequestDto.getPrice())
                .stock(itemRequestDto.getStock())
                .content(itemRequestDto.getContent())
                .image1(itemRequestDto.getImage1())
                .image2(itemRequestDto.getImage2())
                .image3(itemRequestDto.getImage3())
                .build();

        Item savedItem = itemRepository.save(item);

        createKeyword(savedItem.getItemId(), itemRequestDto.getKeywords());

        return responseService.createResponseDto(201, "", savedItem.getItemId());
    }

    private void createKeyword(Long itemId, List<String> keywords) {
        // keyword 생성
        for (String keyword : keywords) {
            if (!keywordRepository.existsByKeyword(keyword)) {
                Keyword saveKeyword = keywordRepository.save(Keyword.builder()
                        .keyword(keyword)
                        .build());

                keywordRepository.save(saveKeyword);
            }
        }

        // keyword - item 연결
        for (String keyword : keywords) {
            Long keywordId = keywordRepository.findByKeyword(keyword).getKeywordId();

            ItemKeyword itemKeyword = ItemKeyword.builder()
                    .itemKeywordID(ItemKeywordID.builder()
                            .keywordId(keywordId)
                            .itemId(itemId)
                            .build())
                    .build();

            itemKeywordRepository.save(itemKeyword);
        }
    }

    private List<ItemListProjection> pagination(List<ItemListProjection> itemList, int pageNum, int pageSize) {
        pageNum = Math.max(pageNum, 1);
        int endIdx = Math.min(pageNum * pageSize, itemList.size());

        itemList = itemList.subList((pageNum - 1) * pageSize, endIdx);

        return itemList;
    }

    /*
     * 아이템 리스트를 정렬하는 메소드
     * n: 최신순,
     * r: 별점순,
     * lp: 낮은 가격순,
     * hp: 높은 가격,
     * s: 판매량 순
     */
    public static List<ItemListProjectionInterface> sortItemList(List<ItemListProjectionInterface> itemList, String sortType) {
        // 기본적으로 최신순으로 정렬
        Comparator<ItemListProjectionInterface> comparator = null;

        if ("r".equals(sortType)) {
            comparator = Comparator.nullsLast(Comparator.comparingInt(ItemListProjectionInterface::getItemAvgReview).reversed());
        } else if ("lp".equals(sortType)) {
            comparator = Comparator.nullsLast(Comparator.comparingInt(ItemListProjectionInterface::getPrice));
        } else if ("hp".equals(sortType)) {
            comparator = Comparator.nullsLast(Comparator.comparingInt(ItemListProjectionInterface::getPrice).reversed());
        } else if ("s".equals(sortType)) {
            comparator = Comparator.nullsLast(Comparator.comparingInt(ItemListProjectionInterface::getSales).reversed());
        }

        // 정렬 적용 및 결과 반환
        return itemList.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }
}
