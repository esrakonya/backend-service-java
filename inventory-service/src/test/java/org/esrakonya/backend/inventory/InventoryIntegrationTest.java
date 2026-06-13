package org.esrakonya.backend.inventory;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.esrakonya.backend.common.test.FullInfrastructureTest;
import org.esrakonya.backend.inventory.domain.InventoryEntity;
import org.esrakonya.backend.inventory.repository.InventoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.print.attribute.standard.Media;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;

public class InventoryIntegrationTest extends FullInfrastructureTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Test
    @DisplayName("Should update stock successfully")
    void shouldUpdateStock() throws Exception {
        Long productId = 101L;
        setupInitialStock(productId, 50);

        mockMvc.perform(post("/api/v1/inventory/sell/"+productId)
                .param("quantity", "10"))
                .andExpect(status().isOk());

        verifyFinalStock(productId,40);
    }

    @Test
    @DisplayName("Should handle concurrent stock deduction correctly with pessimistic locking")
    void shouldHandleConcurrency() throws InterruptedException {
        Long productId = 102L;
        setupInitialStock(productId, 1);

        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                try {
                    mockMvc.perform(post("/api/v1/inventory/sell/"+productId)
                            .param("quantity", "1")
                            .contentType(MediaType.APPLICATION_JSON))
                            .andReturn();
                } catch (Exception ignored) {
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        verifyFinalStock(productId, 0);
    }

    private void setupInitialStock(Long productId, Integer quantity) {
        InventoryEntity inventory = InventoryEntity.builder()
                .productId(productId)
                .availableQuantity(quantity)
                .build();
        inventoryRepository.save(inventory);
    }

    private void verifyFinalStock(Long productId, Integer expectedQuantity) {
        InventoryEntity inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        assertThat(inventory.getAvailableQuantity()).isEqualTo(expectedQuantity);
    }
}
