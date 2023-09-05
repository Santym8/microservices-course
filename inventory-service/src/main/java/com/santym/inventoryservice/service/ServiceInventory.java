package com.santym.inventoryservice.service;

import com.santym.inventoryservice.exception.NoValidInventoryException;
import com.santym.inventoryservice.model.Inventory;
import com.santym.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ServiceInventory {

    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public boolean isInStock(String skuCode) {
        Inventory inventory = inventoryRepository.findBySkuCode(skuCode)
                .orElseThrow(() -> new NoValidInventoryException("Cannot find product by sku code " + skuCode));

        return inventory.getQuantity() > 0;
    }

}
