package com.capstone.mall.controller;

import com.capstone.mall.model.ResponseDto;
import com.capstone.mall.service.item.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class ItemController {

    private final ItemService itemService;

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
}
