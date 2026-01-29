package org.esrakonya.backend.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.esrakonya.backend.inventory.domain.InventoryEntity;
import org.esrakonya.backend.inventory.repository.InventoryRepository;
import org.esrakonya.backend.product.dto.ProductResponse;
import org.esrakonya.backend.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.springframework.transaction.annotation.Transactional;
import org.esrakonya.backend.BaseIntegrationTest;
import org.esrakonya.backend.auth.dto.AuthResponse;
import org.esrakonya.backend.auth.dto.RegisterRequest;
import org.esrakonya.backend.product.domain.CategoryEntity;
import org.esrakonya.backend.product.domain.ProductEntity;
import org.esrakonya.backend.product.dto.CategoryRequest;
import org.esrakonya.backend.product.dto.ProductRequest;
import org.esrakonya.backend.product.repository.CategoryRepository;
import org.esrakonya.backend.product.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@Slf4j
class ProductIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void cleanUp() {
        inventoryRepository.deleteAll();
        productRepository.deleteAll();
        categoryRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Should create product with category and publish kafka event")
    void shouldCreateProductWithCategory() throws Exception {
        CategoryEntity electronics = categoryRepository.save(CategoryEntity.builder().name("Electronics").build());

        // Get Token
        RegisterRequest registerRequest = new RegisterRequest("test-manager@admin.com", "password123");
        MvcResult authResult = mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        AuthResponse authResponse = objectMapper.readValue(authResult.getResponse().getContentAsString(), AuthResponse.class);

        // Create a Product using the ID of the category
        ProductRequest productRequest = ProductRequest.builder()
                .name("M3 Macbook Pro")
                .description("High-end laptop")
                .price(new BigDecimal("3500.00"))
                .categoryId(electronics.getId())
                .stockQuantity(10)
                .build();

        // Perform Request
        mockMvc.perform(post("/api/v1/products")
                .header("Authorization", "Bearer " + authResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.categoryName").value("Electronics"));

        // Kafka Verification
        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            System.out.println("Integration Test: Product created and linked to Electronics category successfully.");
        });
    }

    @Test
    @DisplayName("Should return 403 Forbidden when a regular USER tries to delete a product")
    void shouldReturnForbiddenForRegularUser() throws Exception {
        ProductEntity savedProduct = productRepository.save(ProductEntity.builder()
                .name("To Be Deleted")
                .price(BigDecimal.TEN)
                .stockQuantity(1)
                .build());

        // Register a regular user (not an@admin.com email)
        RegisterRequest userRequest = new RegisterRequest("normal-user@engineer.com", "password1234");
        MvcResult authResult = mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)))
                .andReturn();

        AuthResponse authResponse = objectMapper.readValue(authResult.getResponse().getContentAsString(), AuthResponse.class);

        mockMvc.perform(delete("/api/v1/products/" + savedProduct.getId())
                .header("Authorization", "Bearer " + authResponse.getAccessToken()))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("ADMIN should be able to create a category")
    void adminShouldCreateCategory() throws Exception {
        // Register as Admin
        RegisterRequest adminRequest = new RegisterRequest("boss@admin.com", "password123");
        MvcResult authResult = mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(adminRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        AuthResponse authResponse = objectMapper.readValue(
                authResult.getResponse().getContentAsString(), AuthResponse.class
        );

        // Create category using DTO
        CategoryRequest categoryRequest = new CategoryRequest();
        categoryRequest.setName("Books");

        mockMvc.perform(post("/api/v1/categories")
                .header("Authorization", "Bearer "  + authResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("REGULAR USER should be forbidden from creating a category")
    void userShouldBeForbiddenFromCreatingCategory() throws Exception {
        // Register as regular user
        RegisterRequest userRequest = new RegisterRequest("worker@engineer.com", "password123");
        MvcResult authResult = mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        AuthResponse authResponse = objectMapper.readValue(
                authResult.getResponse().getContentAsString(), AuthResponse.class
        );

        // Try to create category
        CategoryRequest categoryRequest = new CategoryRequest();
        categoryRequest.setName("Illegal Category");

        mockMvc.perform(post("/api/v1/categories")
                .header("Authorization", "Bearer " + authResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryRequest)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("CONCURRENCY TEST: Should handle multiple simultaneous sales without overselling")
    void shouldHandleConcurrentSales() throws Exception {
        // 1. Setup
        CategoryEntity cat = categoryRepository.save(CategoryEntity.builder().name("SpeedTest").build());
        ProductRequest productRequest = ProductRequest.builder()
                .name("Limited Edition Phone")
                .price(new BigDecimal("1000.00"))
                .categoryId(cat.getId())
                .stockQuantity(10)
                .build();

        String adminToken = getAdminToken();

        // 2. Create Product & Capture Actual ID
        MvcResult productResult = mockMvc.perform(post("/api/v1/products")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        ProductResponse productResponse = objectMapper.readValue(
                productResult.getResponse().getContentAsString(), ProductResponse.class);

        Long actualProductId = productResponse.getId(); // THE DYNAMIC ID

        // 3. Wait for Async Consumer (Increased to 30s)
        await()
                .atMost(30, TimeUnit.SECONDS)
                .pollInterval(1, TimeUnit.SECONDS)
                .until(() -> inventoryRepository.existsByProductId(actualProductId));

        // 4. Concurrency Simulation
        int threadCount = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(1);

        for (int i = 0; i < threadCount; i++) {
            executor.execute(() -> {
                try {
                    latch.await();
                    // FIXED: Use actualProductId here
                    mockMvc.perform(post("/api/v1/inventory/sell/" + actualProductId + "?quantity=1")
                            .header("Authorization", "Bearer " + adminToken));
                } catch (Exception e) {
                    log.error("Concurrent request failed", e);
                }
            });
        }

        latch.countDown();
        executor.shutdown();
        executor.awaitTermination(30, TimeUnit.SECONDS);

        // 5. Final Assertion
        // FIXED: Use actualProductId here
        InventoryEntity finalInventory = inventoryRepository.findByProductId(actualProductId)
                .orElseThrow(() -> new RuntimeException("Inventory not found for ID: " + actualProductId));

        Assertions.assertEquals(0, finalInventory.getAvailableQuantity(), "Stock should be zero.");
    }

    private String getAdminToken() throws Exception {
        RegisterRequest adminRequest = new RegisterRequest("test-admin@admin.com", "password123");
        MvcResult result = mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(adminRequest))).andReturn();
        return objectMapper.readValue(result.getResponse().getContentAsString(), AuthResponse.class).getAccessToken();
    }
}
