package com.santym.inventoryservice.service;

import com.santym.inventoryservice.dto.InventoryResponse;
import com.santym.inventoryservice.model.Inventory;
import com.santym.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ServiceInventory {

    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public InventoryResponse isInStock(List<String> skuCode) {

        InventoryResponse inventoryResponse = InventoryResponse.builder()
                .outOfStock(new ArrayList<>())
                .available(new ArrayList<>())
                .notFound(new ArrayList<>())
                .build();

        skuCode.forEach(sku -> {
            Optional<Inventory> inventory = inventoryRepository.findBySkuCode(sku);
            if (inventory.isEmpty()){
                inventoryResponse.getNotFound().add(sku);
            }
            else if (inventory.get().getQuantity() > 0){
                inventoryResponse.getAvailable().add(sku);
            }
            else {
                inventoryResponse.getOutOfStock().add(sku);
            }
        });

        return inventoryResponse;
    }

}
