package com.capstone.mall.service.order;

import com.capstone.mall.model.ResponseDto;
import com.capstone.mall.model.item.Item;
import com.capstone.mall.model.order.*;
import com.capstone.mall.model.order.orderForm.OrderFormDetail;
import com.capstone.mall.repository.JpaItemRepository;
import com.capstone.mall.repository.JpaOrderDetailRepository;
import com.capstone.mall.repository.JpaOrderRepository;
import com.capstone.mall.security.JwtTokenProvider;
import com.capstone.mall.service.response.ResponseService;
import com.capstone.mall.service.util.Paginator;
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

            OrderFormDetail orderFormDetail = OrderFormDetail.builder()
                    .itemId(itemId)
                    .itemName(item.isEmpty() ? "존재하지 않는 상품" : item.get().getName())
                    .image(item.isEmpty() ? "" : item.get().getImage1())
                    .quantity(itemMap.get(itemId))
                    .price(item.isEmpty() ? 0 : item.get().getPrice())
                    .build();

            orderFormDetails.add(orderFormDetail);
        }

        return responseService.createResponseDto(201, "", orderFormDetails);
    }

    @Override
    public ResponseDto updateOrder(Long orderDetailId, OrderRequestDto orderRequestDto, String token) {
        Optional<OrderDetail> orderDetail = orderDetailRepository.findById(orderDetailId);

        if (orderDetail.isEmpty()) {
            return responseService.createResponseDto(200, "orderDetail does not exist", null);
        }

        if (!orderDetail.get().getSellerId().equals(jwtTokenProvider.getUserIdByBearerToken(token))) {
            return responseService.createResponseDto(403, "token does not match", null);
        }

        orderDetail.get().setResult(orderRequestDto.getResult());

        return responseService.createResponseDto(200, "", orderDetail.get().getOrderId());
    }

    @Override
    public ResponseDto getSoldOrderList(String userId, int pageNum, int pageSize, String token) {
        List<OrderDetail> orderDetails = orderDetailRepository.findBySellerId(userId);

        if (orderDetails.isEmpty()) {
            return responseService.createResponseDto(200, "order does not exist", null);
        }

        if (!userId.equals(jwtTokenProvider.getUserIdByBearerToken(token))) {
            return responseService.createResponseDto(403, "token does not match", null);
        }

        // 총 페이지 수
        int totalPage = (int) Math.ceil((double) orderDetails.size() / pageSize);

        // 페이지네이션
        Paginator<OrderDetail> paginator = new Paginator<>();
        orderDetails = paginator.paginate(orderDetails, pageNum, pageSize);

        List<OrderDetailResponseDto> orderDetailResponseDtoList = new ArrayList<>();

        for (OrderDetail orderDetail : orderDetails) {
            Optional<Item> item = itemRepository.findById(orderDetail.getItemId());

            OrderDetailResponseDto orderDetailResponseDto = OrderDetailResponseDto.builder()
                    .orderDetailId(orderDetail.getOrderDetailId())
                    .orderId(orderDetail.getOrderId())
                    .itemId(orderDetail.getItemId())
                    .itemName(item.isEmpty() ? "삭제된 상품" : item.get().getName())
                    .sellerId(orderDetail.getSellerId())
                    .image(item.isEmpty() ? "" : item.get().getImage1())
                    .price(orderDetail.getPrice())
                    .quantity(orderDetail.getQuantity())
                    .result(orderDetail.getResult())
                    .orderDate(orderDetail.getOrderDate())
                    .build();

            orderDetailResponseDtoList.add(orderDetailResponseDto);
        }

        OrderDetailListResponseDto orderListResponseDto = OrderDetailListResponseDto.builder()
                .orders(orderDetailResponseDtoList)
                .totalPage(totalPage)
                .build();

        return responseService.createResponseDto(200, "", orderListResponseDto);
    }

    @Override
    public ResponseDto getPurchaseList(String userId, int pageNum, int pageSize, String token) {
        if (!userId.equals(jwtTokenProvider.getUserIdByBearerToken(token))) {
            return responseService.createResponseDto(403, "token does not match", null);
        }

        // order 를 담기 위해 userId 로 조회한다. (orderId 로 orderDetail 을 조회하기 위함)
        List<Order> orderList = orderRepository.findByUserId(userId);

        // order 를 담기 위한 List
        List<OrderResponseDto> orderResponseDtoList = new ArrayList<>();

        // 총 페이지 수
        int totalPage = (int) Math.ceil((double) orderList.size() / pageSize);

        // 페이지네이션
        Paginator<Order> paginator = new Paginator<>();
        orderList = paginator.paginate(orderList, pageNum, pageSize);

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
                        .itemName(item.isEmpty() ? "삭제된 상품" : item.get().getName())
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

            orderResponseDtoList.add(orderResponseDto);
        }

        OrderListResponseDto orders = OrderListResponseDto.builder()
                .orders(orderResponseDtoList)
                .totalPage(totalPage)
                .build();

        return responseService.createResponseDto(200, "", orders);
    }
}
