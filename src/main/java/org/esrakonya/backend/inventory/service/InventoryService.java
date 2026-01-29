package org.esrakonya.backend.inventory.service;

public interface InventoryService {
    void initializeInventory(Long productId, Integer quantity);

    void updateStock(Long productId, Integer quantitySubtract);
}
