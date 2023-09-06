package com.santym.inventoryservice.controller;

import com.santym.inventoryservice.dto.InventoryResponse;
import com.santym.inventoryservice.service.ServiceInventory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final ServiceInventory serviceInventory;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public InventoryResponse isInStock(@RequestParam List<String> skuCode) {
        return serviceInventory.isInStock(skuCode);
    }

}
