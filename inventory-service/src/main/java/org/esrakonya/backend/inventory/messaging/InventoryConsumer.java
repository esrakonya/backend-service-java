package org.esrakonya.backend.inventory.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.esrakonya.backend.common.event.ProductCreatedEvent;
import org.esrakonya.backend.inventory.service.InventoryService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class InventoryConsumer {
    private final InventoryService inventoryService;

    @KafkaListener(topics = "product-created-topic", groupId = "inventory-group")
    public void handleProductCreated(ProductCreatedEvent event) {
        log.info("INVENTORY SERVICE: Initializing stock for Product ID: {}", event.getProductId());
        inventoryService.initializeInventory(event.getProductId(), event.getQuantity());
    }
}
