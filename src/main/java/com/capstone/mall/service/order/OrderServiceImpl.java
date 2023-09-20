package com.capstone.mall.service.order;

import com.capstone.mall.model.ResponseDto;
import com.capstone.mall.model.item.Item;
import com.capstone.mall.model.order.orderForm.OrderFormDetail;
import com.capstone.mall.repository.JpaItemRepository;
import com.capstone.mall.service.response.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final ResponseService responseService;
    private final JpaItemRepository itemRepository;

    @Override
    public ResponseDto createOrderForm(String userId, String items) {
        Map<Long, Integer> itemMap = new HashMap<>();
        String[] itemArray = items.split(",");
        for (String item : itemArray) {
            String[] itemInfo = item.split(":");
            itemMap.put(Long.parseLong(itemInfo[0]), Integer.parseInt(itemInfo[1]));
        }

        List<OrderFormDetail> orderFormDetails = new ArrayList<>();

        for (Long itemId : itemMap.keySet()) {
            Optional<Item> item = itemRepository.findById(itemId);

            OrderFormDetail orderFormDetail = OrderFormDetail.builder()
                    .itemId(itemId)
                    .itemName(item.isEmpty() ? "존재하지 않는 상품" : item.get().getName())
                    .image(item.isEmpty() ? "" : item.get().getImage1())
                    .quantity(itemMap.get(itemId))
                    .price(item.isEmpty() ? 0 : item.get().getPrice())
                    .build();

            orderFormDetails.add(orderFormDetail);
        }

        return responseService.createResponseDto(200, "", orderFormDetails);
    }
}
