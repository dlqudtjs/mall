package com.capstone.mall.service.item;

import com.capstone.mall.model.ResponseDto;
import com.capstone.mall.model.item.*;
import com.capstone.mall.model.itemKeyword.ItemKeyword;
import com.capstone.mall.model.itemKeyword.ItemKeywordID;
import com.capstone.mall.model.keyword.Keyword;
import com.capstone.mall.repository.JpaItemKeywordRepository;
import com.capstone.mall.repository.JpaItemRepository;
import com.capstone.mall.repository.JpaKeywordRepository;
import com.capstone.mall.security.JwtTokenProvider;
import com.capstone.mall.service.response.ResponseServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemServiceImpl implements ItemService {

    private final JpaItemRepository itemRepository;
    private final ResponseServiceImpl responseService;
    private final JpaKeywordRepository keywordRepository;
    private final JpaItemKeywordRepository itemKeywordRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public ResponseDto readItem(Long itemId) {
        if (!itemRepository.existsById(itemId)) {
            return responseService.createResponseDto(200, "", null);
        }

        ItemProjectionInterface item = itemRepository.getItemDetailByItemId(itemId);

        ItemResponseDto itemResponseDto = ItemResponseDto.builder()
                .itemId(item.getItemId())
                .sellerId(item.getSellerId())
                .categoryId(item.getCategoryId())
                .name(item.getName())
                .image1(item.getImage1())
                .image2(item.getImage2())
                .image3(item.getImage3())
                .content(item.getContent())
                .price(item.getPrice())
                .rate(item.getAvgRating())
                .reviewCount(item.getReviewCount())
                .stock(item.getStock())
                .build();

        return responseService.createResponseDto(200, "", itemResponseDto);
    }

    @Override
    // 검색으로 아이템 리스트 조회
    public ResponseDto readItemListBySearch(String search, int pageNum, int pageSize, String sort, String sortType) {
        Sort pageSort = getSort(sort, sortType);
        Pageable pageable = getPageable(pageNum, pageSize, pageSort);

        Page<ItemProjectionInterface> items = itemRepository.getItemListByKeyword(search, pageable);

        // Interface 매핑
        List<ItemProjection> itemList = getItems(items);

        ItemProjectionListResponseDto itemResponseDto = ItemProjectionListResponseDto.builder()
                .items(itemList)
                .totalPage(items.getTotalPages())
                .build();

        return responseService.createResponseDto(200, "", itemResponseDto);
    }


    @Override
    // 카테고리로 아이템 리스트 조회
    public ResponseDto readItemListByCategoryId(Long categoryId, int pageNum, int pageSize, String sort, String sortType) {
        Sort pageSort = getSort(sort, sortType);
        Pageable pageable = getPageable(pageNum, pageSize, pageSort);

        Page<ItemProjectionInterface> items = itemRepository.getItemListByCategoryId(categoryId, pageable);

        // Interface 매핑
        List<ItemProjection> itemList = getItems(items);

        ItemProjectionListResponseDto itemResponseDto = ItemProjectionListResponseDto.builder()
                .items(itemList)
                .totalPage(items.getTotalPages())
                .build();

        return responseService.createResponseDto(200, "", itemResponseDto);
    }

    // 판매자가 등록한 아이템 리스트 조회
    @Override
    public ResponseDto readItemListBySellerId(String sellerId, int pageNum, int pageSize, String token) {
        if (!jwtTokenProvider.getUserIdByBearerToken(token).equals(sellerId)) {
            return responseService.createResponseDto(403, "token does not match", null);
        }

        Pageable pageable = getPageable(pageNum, pageSize);

        Page<ItemProjectionInterface> items = itemRepository.getItemsListBySellerId(sellerId, pageable);

        // Interface 매핑
        List<ItemProjection> itemList = getItems(items);

        ItemProjectionListResponseDto itemResponseDto = ItemProjectionListResponseDto.builder()
                .items(itemList)
                .totalPage(items.getTotalPages())
                .build();

        return responseService.createResponseDto(200, "", itemResponseDto);
    }

    @Override
    public ResponseDto createItem(String sellerId, ItemCreateRequestDto itemRequestDto) {
        Item item = Item.builder()
                .sellerId(sellerId)
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

    @Override
    public ResponseDto updateItem(Long itemId, ItemUpdateRequestDto itemRequestDto, String token) {
        Optional<Item> item = itemRepository.findById(itemId);

        if (item.isEmpty()) {
            return responseService.createResponseDto(200, "item does not exist", null);
        }

        if (!jwtTokenProvider.getUserIdByBearerToken(token).equals(item.get().getSellerId())) {
            return responseService.createResponseDto(403, "token does not match", null);
        }

        item.get().setSellerId(itemRequestDto.getSellerId());
        item.get().setName(itemRequestDto.getName());
        item.get().setCategoryId(itemRequestDto.getCategoryId());
        item.get().setPrice(itemRequestDto.getPrice());
        item.get().setStock(itemRequestDto.getStock());
        item.get().setContent(itemRequestDto.getContent());
        item.get().setImage1(itemRequestDto.getImage1());
        item.get().setImage2(itemRequestDto.getImage2());
        item.get().setImage3(itemRequestDto.getImage3());

        return responseService.createResponseDto(200, "", itemId);
    }

    @Override
    public ResponseDto deleteItem(Long itemId, String token) {
        Optional<Item> item = itemRepository.findById(itemId);

        if (item.isEmpty()) {
            return responseService.createResponseDto(200, "item does not exist", null);
        }

        if (!jwtTokenProvider.getUserIdByBearerToken(token).equals(item.get().getSellerId())) {
            return responseService.createResponseDto(403, "token does not match", null);
        }

        itemRepository.deleteById(itemId);

        return responseService.createResponseDto(200, "", itemId);
    }

    private List<ItemProjection> getItems(Page<ItemProjectionInterface> items) {
        List<ItemProjection> itemList = new ArrayList<>();
        for (ItemProjectionInterface item : items.getContent()) {
            itemList.add(ItemProjection.builder()
                    .itemId(item.getItemId())
                    .name(item.getName())
                    .image1(item.getImage1())
                    .price(item.getPrice())
                    .content(item.getContent())
                    .createdAt(item.getCreatedAt())
                    .updatedAt(item.getUpdatedAt())
                    .avgRating(item.getAvgRating())
                    .sales(item.getSales())
                    .reviewCount(item.getReviewCount())
                    .stock(item.getStock())
                    .build());
        }

        return itemList;
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

    private Pageable getPageable(int pageNum, int pageSize) {
        pageNum = Math.max(pageNum, 0);
        pageSize = Math.max(pageSize, 1);

        return PageRequest.of(pageNum, pageSize);
    }

    private Pageable getPageable(int pageNum, int pageSize, Sort pageSort) {
        pageNum = Math.max(pageNum, 0);
        pageSize = Math.max(pageSize, 1);

        return PageRequest.of(pageNum, pageSize, pageSort);
    }

    private Sort getSort(String sort, String sortType) {
        // 정렬 기준이 rate, createdAt이 아닐 경우 default 로 createdAt 으로 정렬
        if (!sort.equals("avgRating") && !sort.equals("createdAt") &&
                !sort.equals("price") && !sort.equals("sales") &&
                !sort.equals("stock")) {
            sort = "createdAt";
        }

        if (sortType.equals("asc")) {
            return Sort.by(sort).descending();
        }

        return Sort.by(sort).ascending();
    }
}
