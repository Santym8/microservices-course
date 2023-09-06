package com.santym.inventoryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InventoryResponse {
    private List<String> available;
    private List<String> outOfStock;
    private List<String> notFound;
}
