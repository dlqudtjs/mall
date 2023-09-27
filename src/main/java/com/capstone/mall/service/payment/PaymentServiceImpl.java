package com.capstone.mall.service.payment;

import com.capstone.mall.model.ResponseDto;
import com.capstone.mall.model.item.Item;
import com.capstone.mall.model.order.Order;
import com.capstone.mall.model.order.orderDetail.OrderDetail;
import com.capstone.mall.model.payment.PaymentRequestDto;
import com.capstone.mall.repository.JpaItemRepository;
import com.capstone.mall.repository.JpaOrderDetailRepository;
import com.capstone.mall.repository.JpaOrderRepository;
import com.capstone.mall.service.response.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final ResponseService responseService;
    private final JpaOrderRepository orderRepository;
    private final JpaOrderDetailRepository orderDetailRepository;
    private final JpaItemRepository itemRepository;

    @Override
    @Transactional
    public ResponseDto payment(String userId, String items, PaymentRequestDto paymentRequestDto) {
        Map<Long, Integer> itemMap = getItemMap(items);

        // 재고 확인
        for (Long itemId : itemMap.keySet()) {
            Optional<Item> item = itemRepository.findById(itemId);

            if (item.isEmpty()) {
                return responseService.createResponseDto(200, "삭제된 상품이 존재합니다.", null);
            }

            if (item.get().getStock() < itemMap.get(itemId)) {
                return responseService.createResponseDto(200, "재고가 부족합니다.", null);
            }

            item.get().setStock(item.get().getStock() - itemMap.get(itemId));
        }

        // 주문 생성
        Order order = createOrder(userId, paymentRequestDto, itemMap);

        // 주문 상세 생성
        createOrderDetail(order, itemMap);

        return responseService.createResponseDto(200, "", order.getOrderId());
    }

    private void createOrderDetail(Order order, Map<Long, Integer> itemMap) {
        for (Long itemId : itemMap.keySet()) {
            Optional<Item> item = itemRepository.findById(itemId);

            if (item.isEmpty()) {
                continue;
            }

            OrderDetail orderDetail = OrderDetail.builder()
                    .orderId(order.getOrderId())
                    .itemId(itemId)
                    .sellerId(item.get().getSellerId())
                    .price(item.get().getPrice())
                    .quantity(itemMap.get(itemId))
                    .result(0)
                    .build();

            orderDetailRepository.save(orderDetail);
        }
    }

    private Order createOrder(String userId, PaymentRequestDto paymentRequestDto, Map<Long, Integer> itemMap) {
        // 총 상품 개수
        int totalCount = itemMap.values().stream().mapToInt(Integer::intValue).sum();

        // 총 상품 가격
        int totalPrice = itemMap.keySet().stream().mapToInt(itemId -> {
            Optional<Item> item = itemRepository.findById(itemId);
            return item.map(value -> value.getPrice() * itemMap.get(itemId)).orElse(0);
        }).sum();

        Order order = Order.builder()
                .userId(userId)
                .totalCount(totalCount)
                .totalPrice(totalPrice)
                .recipient(paymentRequestDto.getRecipient())
                .address(paymentRequestDto.getAddress())
                .detailAddress(paymentRequestDto.getDetailAddress())
                .phone(paymentRequestDto.getPhone())
                .zipCode(paymentRequestDto.getZipCode())
                .build();

        return orderRepository.save(order);
    }


    private Map<Long, Integer> getItemMap(String items) {
        Map<Long, Integer> itemMap = new HashMap<>();
        String[] itemArray = items.split(",");

        for (String item : itemArray) {
            String[] itemInfo = item.split(":");
            itemMap.put(Long.parseLong(itemInfo[0]), Integer.parseInt(itemInfo[1]));
        }

        return itemMap;
    }
}
