package com.capstone.mall.service.item;

import com.capstone.mall.model.ResponseDto;
import com.capstone.mall.model.item.Item;
import com.capstone.mall.model.item.ItemRequestDto;
import com.capstone.mall.model.item.ItemResponseDto;
import com.capstone.mall.repository.JpaItemRepository;
import com.capstone.mall.service.response.ResponseServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemServiceImpl implements ItemService {

    private final JpaItemRepository itemRepository;
    private final ResponseServiceImpl responseService;

    @Override
    // 검색으로 아이템 리스트 조회
    public ResponseDto readItemList(String search, int pageNum, int pageSize, String sortType) {


        return null;
    }

    @Override
    // 카테고리로 아이템 리스트 조회
    public ResponseDto readItemList(Long categoryId, int pageNum, int pageSize, String sortType) {
        List<Item> items = itemRepository.callGetItemsByCategoryId(categoryId);

        // 정렬
        items = sortItemList(items, sortType);

        // 총 페이지 수
        int totalPage = (int) Math.ceil((double) items.size() / pageSize);

        // 페이지네이션
        items = pagination(items, pageNum, pageSize);

        ItemResponseDto itemResponseDto = ItemResponseDto.builder()
                .items(items)
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

        return responseService.createResponseDto(201, "", savedItem.getItemId());
    }

    private List<Item> pagination(List<Item> itemList, int pageNum, int pageSize) {
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
    private List<Item> sortItemList(List<Item> itemList, String sortType) {
        if (sortType.equals("n")) {
            itemList.sort((o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt()));
            return itemList;
        }

        if (sortType.equals("r")) {
            itemList.sort((o1, o2) -> o2.getItemAvgReview() - (o1.getItemAvgReview()));
            return itemList;
        }

        if (sortType.equals("lp")) {
            itemList.sort(Comparator.comparingInt(Item::getPrice));
            return itemList;
        }

        if (sortType.equals("hp")) {
            itemList.sort((o1, o2) -> o2.getPrice() - (o1.getPrice()));
            return itemList;
        }

        if (sortType.equals("s")) {
            itemList.sort((o1, o2) -> o2.getSales() - (o1.getSales()));
            return itemList;
        }
        // sortType 이 잘못된 경우 최신순으로 정렬
        else {
            itemList.sort((o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt()));
            return itemList;
        }
    }
}
