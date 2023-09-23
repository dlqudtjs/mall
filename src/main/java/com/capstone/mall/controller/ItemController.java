package com.capstone.mall.controller;

import com.capstone.mall.model.ResponseDto;
import com.capstone.mall.model.item.ItemRequestDto;
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
                                                            @RequestParam(required = false, defaultValue = "n") String sortType,
                                                            @RequestParam(required = false, defaultValue = "1") int pageNum,
                                                            @RequestParam(required = false, defaultValue = "10") int pageSize) {
        ResponseDto responseDto = itemService.readItemList(search, pageNum, pageSize, sortType);

        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @GetMapping("/public/categories/{categoryId}/items")
    public ResponseEntity<ResponseDto> readItemListByCategory(@PathVariable Long categoryId,
                                                              @RequestParam(required = false, defaultValue = "n") String sortType,
                                                              @RequestParam(required = false, defaultValue = "1") int pageNum,
                                                              @RequestParam(required = false, defaultValue = "10") int pageSize) {

        ResponseDto responseDto = itemService.readItemList(categoryId, pageNum, pageSize, sortType);

        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @GetMapping("/sellers/items/{sellerId}")
    public ResponseEntity<ResponseDto> readItemListBySellerId(@PathVariable Long sellerId,
                                                              @RequestParam(required = false, defaultValue = "1") int pageNum,
                                                              @RequestParam(required = false, defaultValue = "10") int pageSize) {
        ResponseDto responseDto = itemService.readItemListBySellerId(sellerId, pageNum, pageSize);

        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @PostMapping("/sellers/items/{sellerId}")
    public ResponseEntity<ResponseDto> createItem(@PathVariable String sellerId, @RequestBody ItemRequestDto itemRequestDto) {
        ResponseDto responseDto = itemService.createItem(sellerId, itemRequestDto);

        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @PatchMapping("/sellers/items/{itemId}")
    public ResponseEntity<ResponseDto> updateItem(@PathVariable Long itemId, @RequestBody ItemRequestDto itemRequestDto) {
        ResponseDto responseDto = itemService.updateItem(itemId, itemRequestDto);

        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @DeleteMapping("/sellers/items/{itemId}")
    public ResponseEntity<ResponseDto> deleteItem(@PathVariable Long itemId) {
        ResponseDto responseDto = itemService.deleteItem(itemId);

        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }
}
