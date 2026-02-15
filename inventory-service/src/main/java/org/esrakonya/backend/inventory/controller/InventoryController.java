package org.esrakonya.backend.inventory.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.esrakonya.backend.common.core.dto.MessageResponse;
import org.esrakonya.backend.inventory.service.InventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
@Tag(name = "Inventory Management")
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping("/sell/{productId}")
    @Operation(
            summary = "Process a sale",
            description = "Decreases stock using Pessimistic Locking to ensure data integrity."
    )
    public ResponseEntity<MessageResponse> processSale(@PathVariable Long productId, @RequestParam Integer quantity) {
        inventoryService.updateStock(productId, quantity);
        return ResponseEntity.ok(new MessageResponse("Sale processed and stock updated successfully."));
    }
}
