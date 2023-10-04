package com.capstone.mall.controller;

import com.capstone.mall.model.ResponseDto;
import com.capstone.mall.model.item.ItemCreateRequestDto;
import com.capstone.mall.model.item.ItemUpdateRequestDto;
import com.capstone.mall.service.item.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/public/items/{itemId}")
    public ResponseEntity<ResponseDto> readItem(@PathVariable Long itemId) {
        ResponseDto responseDto = itemService.readItem(itemId);

        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @GetMapping("/public/items")
    public ResponseEntity<ResponseDto> readItemListBySearch(@RequestParam(required = true) String search,
                                                            @RequestParam(required = false, defaultValue = "createdAt") String sort,
                                                            @RequestParam(required = false, defaultValue = "desc") String sortType,
                                                            @RequestParam(required = false, defaultValue = "0") int pageNum,
                                                            @RequestParam(required = false, defaultValue = "10") int pageSize) {
        ResponseDto responseDto = itemService.readItemListBySearch(search, pageNum, pageSize, sort, sortType);

        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @GetMapping("/public/categories/{categoryId}/items")
    public ResponseEntity<ResponseDto> readItemListByCategory(@PathVariable Long categoryId,
                                                              @RequestParam(required = false, defaultValue = "createdAt") String sort,
                                                              @RequestParam(required = false, defaultValue = "desc") String sortType,
                                                              @RequestParam(required = false, defaultValue = "0") int pageNum,
                                                              @RequestParam(required = false, defaultValue = "10") int pageSize) {

        ResponseDto responseDto = itemService.readItemListByCategoryId(categoryId, pageNum, pageSize, sort, sortType);

        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    // 판매중인 상품 목록 조회
    @GetMapping("/sellers/items/{sellerId}")
    public ResponseEntity<ResponseDto> readItemListBySellerId(@PathVariable String sellerId,
                                                              @RequestParam(required = false, defaultValue = "0") int pageNum,
                                                              @RequestParam(required = false, defaultValue = "10") int pageSize,
                                                              @RequestHeader("Authorization") String token) {
        ResponseDto responseDto = itemService.readItemListBySellerId(sellerId, pageNum, pageSize, token);

        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @PostMapping("/sellers/items/{sellerId}")
    public ResponseEntity<ResponseDto> createItem(@PathVariable String sellerId, @RequestBody ItemCreateRequestDto itemRequestDto) {
        ResponseDto responseDto = itemService.createItem(sellerId, itemRequestDto);

        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @PatchMapping("/sellers/items/{itemId}")
    public ResponseEntity<ResponseDto> updateItem(@PathVariable Long itemId,
                                                  @RequestBody ItemUpdateRequestDto itemRequestDto,
                                                  @RequestHeader("Authorization") String token) {
        ResponseDto responseDto = itemService.updateItem(itemId, itemRequestDto, token);

        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @DeleteMapping("/sellers/items/{itemId}")
    public ResponseEntity<ResponseDto> deleteItem(@PathVariable Long itemId, @RequestHeader("Authorization") String token) {
        ResponseDto responseDto = itemService.deleteItem(itemId, token);

        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }
}
