package com.capstone.mall.service.order;

import com.capstone.mall.model.ResponseDto;
import com.capstone.mall.model.item.Item;
import com.capstone.mall.model.order.Order;
import com.capstone.mall.model.order.OrderListResponseDto;
import com.capstone.mall.model.order.OrderResponseDto;
import com.capstone.mall.model.order.OrderUpdateRequestDto;
import com.capstone.mall.model.order.orderDetail.OrderDetail;
import com.capstone.mall.model.order.orderDetail.OrderDetailListResponseDto;
import com.capstone.mall.model.order.orderDetail.OrderDetailResponseDto;
import com.capstone.mall.model.order.orderForm.OrderFormDetail;
import com.capstone.mall.repository.JpaItemRepository;
import com.capstone.mall.repository.JpaOrderDetailRepository;
import com.capstone.mall.repository.JpaOrderRepository;
import com.capstone.mall.security.JwtTokenProvider;
import com.capstone.mall.service.response.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private final JwtTokenProvider jwtTokenProvider;

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

            if (item.isEmpty()) {
                continue;
            }

            OrderFormDetail orderFormDetail = OrderFormDetail.builder()
                    .itemId(itemId)
                    .itemName(item.get().getName())
                    .image(item.get().getImage1())
                    .quantity(itemMap.get(itemId))
                    .price(item.get().getPrice())
                    .build();

            orderFormDetails.add(orderFormDetail);
        }

        return responseService.createResponseDto(201, "", orderFormDetails);
    }

    @Override
    public ResponseDto updateOrder(Long orderDetailId, OrderUpdateRequestDto orderUpdateRequestDto, String token) {
        Optional<OrderDetail> orderDetail = orderDetailRepository.findById(orderDetailId);

        if (orderDetail.isEmpty()) {
            return responseService.createResponseDto(200, "order does not exist", null);
        }

        if (!jwtTokenProvider.getUserIdByBearerToken(token).equals(orderDetail.get().getSellerId())) {
            return responseService.createResponseDto(403, "token does not match", null);
        }

        orderDetail.get().setResult(orderUpdateRequestDto.getResult());

        return responseService.createResponseDto(200, "", orderDetailId);
    }

    @Override
    public ResponseDto getSoldOrderList(String userId, int pageNum, int pageSize, String token) {
        // 조회하는 유저와 토큰의 유저가 일치하는지 확인
        if (!jwtTokenProvider.getUserIdByBearerToken(token).equals(userId)) {
            return responseService.createResponseDto(403, "token does not match", null);
        }

        Pageable pageable = getPageable(pageNum, pageSize);
        Page<OrderDetail> orderDetails = orderDetailRepository.findAllBySellerId(userId, pageable);

        List<OrderDetailResponseDto> orderDetailResponseDtoList = new ArrayList<>();
        for (OrderDetail orderDetail : orderDetails.getContent()) {
            Optional<Item> item = itemRepository.findById(orderDetail.getItemId());

            if (item.isEmpty()) {
                continue;
            }

            OrderDetailResponseDto orderDetailResponseDto = OrderDetailResponseDto.builder()
                    .orderDetailId(orderDetail.getOrderDetailId())
                    .orderId(orderDetail.getOrderId())
                    .itemId(orderDetail.getItemId())
                    .itemName(item.get().getName())
                    .sellerId(orderDetail.getSellerId())
                    .image(item.get().getImage1())
                    .price(orderDetail.getPrice())
                    .quantity(orderDetail.getQuantity())
                    .result(orderDetail.getResult())
                    .orderDate(orderDetail.getOrderDate())
                    .build();

            orderDetailResponseDtoList.add(orderDetailResponseDto);
        }

        OrderDetailListResponseDto orderListResponseDto = OrderDetailListResponseDto.builder()
                .orders(orderDetailResponseDtoList)
                .totalPage(orderDetails.getTotalPages())
                .build();

        return responseService.createResponseDto(200, "", orderListResponseDto);
    }

    @Override
    public ResponseDto getPurchaseList(String userId, int pageNum, int pageSize, String token) {
        // 조회하는 유저와 토큰의 유저가 일치하는지 확인
        if (!jwtTokenProvider.getUserIdByBearerToken(token).equals(userId)) {
            return responseService.createResponseDto(403, "token does not match", null);
        }

        Pageable pageable = getPageable(pageNum, pageSize);
        // order 를 담기 위해 userId 로 조회한다. (orderId 로 orderDetail 을 조회하기 위함)
        Page<Order> orderList = orderRepository.findAllByUserId(userId, pageable);

        // order 를 담기 위한 List
        List<OrderResponseDto> orderResponseDtoList = new ArrayList<>();
        for (Order order : orderList.getContent()) {
            // orderDetail 을 담기 위해 orderId 로 조회한다.
            List<OrderDetail> orderDetailList = orderDetailRepository.findAllByOrderId(order.getOrderId());

            // orderDetail 을 담기 위한 List
            List<OrderDetailResponseDto> orderDetails = new ArrayList<>();
            for (OrderDetail orderDetail : orderDetailList) {
                Optional<Item> item = itemRepository.findById(orderDetail.getItemId());

                if (item.isEmpty()) {
                    continue;
                }

                OrderDetailResponseDto orderDetailResponseDto = OrderDetailResponseDto.builder()
                        .orderDetailId(orderDetail.getOrderDetailId())
                        .orderId(orderDetail.getOrderId())
                        .itemId(orderDetail.getItemId())
                        .itemName(item.get().getName())
                        .sellerId(orderDetail.getSellerId())
                        .image(item.get().getImage1())
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

            orderResponseDtoList.add(orderResponseDto);
        }

        OrderListResponseDto orders = OrderListResponseDto.builder()
                .orders(orderResponseDtoList)
                .totalPage(orderList.getTotalPages())
                .build();

        return responseService.createResponseDto(200, "", orders);
    }

    private Pageable getPageable(int pageNum, int pageSize) {
        pageNum = Math.max(pageNum, 0);
        pageSize = Math.max(pageSize, 1);

        return PageRequest.of(pageNum, pageSize);
    }
}
