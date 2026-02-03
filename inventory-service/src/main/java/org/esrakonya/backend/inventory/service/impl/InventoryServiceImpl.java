package org.esrakonya.backend.inventory.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.esrakonya.backend.common.exception.InsufficientStockException;
import org.esrakonya.backend.common.exception.ResourceNotFoundException;
import org.esrakonya.backend.inventory.domain.InventoryEntity;
import org.esrakonya.backend.inventory.repository.InventoryRepository;
import org.esrakonya.backend.inventory.service.InventoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    @Override
    @Transactional
    public void initializeInventory(Long productId, Integer quantity) {
        if (inventoryRepository.existsByProductId(productId)) {
            log.warn("Inventory already initialized for product ID: {}. Skipping.", productId);
            return;
        }

        InventoryEntity inventory = InventoryEntity.builder()
                .productId(productId)
                .availableQuantity(quantity)
                .build();

        inventoryRepository.save(inventory);
        log.info("Inventory record created for Product ID: {}", productId);
    }

    @Override
    @Transactional
    public void updateStock(Long productId, Integer quantitySubtract) {
        log.info("Attempting to update stock for Porduct ID: {} (Subtracting {})", productId, quantitySubtract);

        InventoryEntity inventory = inventoryRepository.findByProductIdWithLock(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found for product: " + productId));

        if (inventory.getAvailableQuantity() < quantitySubtract) {
            throw new InsufficientStockException("Product " + productId + " has insufficient stock.");
        }

        int newStock = inventory.getAvailableQuantity() - quantitySubtract;
        inventory.setAvailableQuantity(newStock);

        inventoryRepository.save(inventory);

        log.info("Stock updated. New available quantity: {}", newStock);

        // TODO: Publish ProductSoldEvent to Kafka here later
    }
}
