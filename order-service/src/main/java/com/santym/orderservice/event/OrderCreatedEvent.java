package com.santym.orderservice.event;

import com.santym.orderservice.dto.OrderLineItemsDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreatedEvent {
    private String orderNumber;
    private List<OrderLineItemsDto> items;
}
