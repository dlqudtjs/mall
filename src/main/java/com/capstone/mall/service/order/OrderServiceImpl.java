package com.capstone.mall.service.order;

import com.capstone.mall.model.ResponseDto;
import com.capstone.mall.model.item.Item;
import com.capstone.mall.model.order.*;
import com.capstone.mall.model.order.orderForm.OrderFormDetail;
import com.capstone.mall.repository.JpaItemRepository;
import com.capstone.mall.repository.JpaOrderDetailRepository;
import com.capstone.mall.repository.JpaOrderRepository;
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
    private final JpaOrderDetailRepository orderDetailRepository;
    private final JpaOrderRepository orderRepository;

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

    @Override
    public ResponseDto updateOrder(Long orderDetailId, OrderRequestDto orderRequestDto) {
        Optional<OrderDetail> orderDetail = orderDetailRepository.findById(orderDetailId);

        if (orderDetail.isEmpty()) {
            return responseService.createResponseDto(200, "존재하지 않는 주문입니다.", null);
        }

        orderDetail.get().setResult(orderRequestDto.getResult());

        return responseService.createResponseDto(200, "", orderDetail.get().getOrderId());
    }

    @Override
    public ResponseDto getSoldOrderList(String userId) {
        List<OrderDetail> orderDetails = orderDetailRepository.findBySellerId(userId);

        if (orderDetails.isEmpty()) {
            return responseService.createResponseDto(200, "주문 내역이 없습니다.", null);
        }

        List<OrderDetailResponseDto> orders = new ArrayList<>();

        for (OrderDetail orderDetail : orderDetails) {
            Optional<Item> item = itemRepository.findById(orderDetail.getItemId());

            OrderDetailResponseDto orderDetailResponseDto = OrderDetailResponseDto.builder()
                    .orderDetailId(orderDetail.getOrderDetailId())
                    .orderId(orderDetail.getOrderId())
                    .itemId(orderDetail.getItemId())
                    .sellerId(orderDetail.getSellerId())
                    .image(item.isEmpty() ? "" : item.get().getImage1())
                    .price(orderDetail.getPrice())
                    .quantity(orderDetail.getQuantity())
                    .result(orderDetail.getResult())
                    .orderDate(orderDetail.getOrderDate())
                    .build();

            orders.add(orderDetailResponseDto);
        }

        return responseService.createResponseDto(200, "", orders);
    }

    @Override
    public ResponseDto getPurchaseList(String userId) {
        // order 를 담기 위한 List
        List<OrderResponseDto> orders = new ArrayList<>();

        // order 를 담기 위해 userId 로 조회한다.
        List<Order> orderList = orderRepository.findByUserId(userId);

        for (Order order : orderList) {
            // orderDetail 을 담기 위한 List
            List<OrderDetailResponseDto> orderDetails = new ArrayList<>();

            // orderDetail 을 담기 위해 orderId 로 조회한다.
            List<OrderDetail> orderDetailList = orderDetailRepository.findByOrderId(order.getOrderId());

            for (OrderDetail orderDetail : orderDetailList) {
                Optional<Item> item = itemRepository.findById(orderDetail.getItemId());

                OrderDetailResponseDto orderDetailResponseDto = OrderDetailResponseDto.builder()
                        .orderDetailId(orderDetail.getOrderDetailId())
                        .orderId(orderDetail.getOrderId())
                        .itemId(orderDetail.getItemId())
                        .sellerId(orderDetail.getSellerId())
                        .image(item.isEmpty() ? "" : item.get().getImage1())
                        .price(orderDetail.getPrice())
                        .quantity(orderDetail.getQuantity())
                        .result(orderDetail.getResult())
                        .orderDate(orderDetail.getOrderDate())
                        .build();

                // orderDetail 을 orderDetails 에 담는다.
                orderDetails.add(orderDetailResponseDto);
            }

            OrderResponseDto orderResponseDto = OrderResponseDto.builder()
                    .orderId(order.getOrderId())
                    .orderDate(order.getOrderDate())
                    .totalPrice(order.getTotalPrice())
                    .orderDetails(orderDetails)
                    .build();

            orders.add(orderResponseDto);
        }

        return responseService.createResponseDto(200, "", orders);
    }
}
