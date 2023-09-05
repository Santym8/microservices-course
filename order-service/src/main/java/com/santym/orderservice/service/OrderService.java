package com.santym.orderservice.service;

import com.santym.orderservice.dto.OrderRequest;
import com.santym.orderservice.model.Order;
import com.santym.orderservice.model.OrderLineItems;
import com.santym.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;

    public void createOrder(OrderRequest orderRequest) {
        Order order = Order.builder()
                .orderNumber(UUID.randomUUID().toString())
                .orderLineItemsList(
                        orderRequest.getOrderLineItemsList().stream().map(orderLineItemsDto ->
                                OrderLineItems.builder()
                                        .skuCode(orderLineItemsDto.getSkuCode())
                                        .price(orderLineItemsDto.getPrice())
                                        .quantity(orderLineItemsDto.getQuantity())
                                        .build())
                                .toList())
                .build();

        orderRepository.save(order);
    }

    public List<Order> getOrders() {
        return orderRepository.findAll();
    }
}
