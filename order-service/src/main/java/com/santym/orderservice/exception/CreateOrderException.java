package com.santym.orderservice.exception;

import com.santym.orderservice.dto.InventoryResponse;
import lombok.Getter;

@Getter
public class CreateOrderException extends RuntimeException{

    private final InventoryResponse body;

    public CreateOrderException(String message, InventoryResponse body) {
        super(message);
        this.body = body;
    }
}
