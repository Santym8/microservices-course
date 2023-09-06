package com.santym.orderservice.service;

import com.santym.orderservice.dto.InventoryResponse;
import com.santym.orderservice.dto.OrderRequest;
import com.santym.orderservice.exception.CreateOrderException;
import com.santym.orderservice.model.Order;
import com.santym.orderservice.model.OrderLineItems;
import com.santym.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;

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

        List<String> skuCodes = order.getOrderLineItemsList().stream()
                .map(OrderLineItems::getSkuCode).toList();
        InventoryResponse inventoryResponse = webClientBuilder.build().get()
                .uri("http://inventory-service/api/v1/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse.class)
                .block();

        if(inventoryResponse == null ||
                !inventoryResponse.getNotFound().isEmpty() ||
                !inventoryResponse.getOutOfStock().isEmpty()) {
            throw new CreateOrderException("Invalid inventory", inventoryResponse);
        }

        orderRepository.save(order);
    }

    public List<Order> getOrders() {
        return orderRepository.findAll();
    }
}
