package org.esrakonya.backend.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.esrakonya.backend.BaseIntegrationTest;
import org.esrakonya.backend.auth.dto.AuthResponse;
import org.esrakonya.backend.auth.dto.RegisterRequest;
import org.esrakonya.backend.inventory.repository.InventoryRepository;
import org.esrakonya.backend.product.domain.CategoryEntity;
import org.esrakonya.backend.product.domain.ProductEntity;
import org.esrakonya.backend.product.dto.CategoryRequest;
import org.esrakonya.backend.product.dto.ProductRequest;
import org.esrakonya.backend.product.dto.ProductResponse;
import org.esrakonya.backend.product.repository.CategoryRepository;
import org.esrakonya.backend.product.repository.ProductRepository;
import org.esrakonya.backend.user.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.*;

import static org.awaitility.Awaitility.await;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
class ProductIntegrationTest extends BaseIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private InventoryRepository inventoryRepository;
    @Autowired private UserRepository userRepository;

    @AfterEach
    void cleanUp() {
        inventoryRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        categoryRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("Should create product and verify async inventory setup")
    void shouldCreateProductWithCategory() throws Exception {
        CategoryEntity cat = categoryRepository.save(CategoryEntity.builder().name("Electronics-" + UUID.randomUUID()).build());
        String token = getAdminToken();

        ProductRequest req = ProductRequest.builder()
                .name("Test Product")
                .price(new BigDecimal("100.00"))
                .categoryId(cat.getId())
                .stockQuantity(50)
                .build();

        MvcResult result = mockMvc.perform(post("/api/v1/products")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andReturn();

        ProductResponse res = objectMapper.readValue(result.getResponse().getContentAsString(), ProductResponse.class);

        // Wait for Kafka Consumer
        await().atMost(15, TimeUnit.SECONDS).until(() -> inventoryRepository.existsByProductId(res.getId()));
    }

    @Test
    @DisplayName("ADMIN should be able to create a category")
    void adminShouldCreateCategory() throws Exception {
        String token = getAdminToken();
        CategoryRequest req = new CategoryRequest();
        req.setName("Books-" + UUID.randomUUID());

        mockMvc.perform(post("/api/v1/categories")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Forbidden test for regular user")
    void userShouldBeForbidden() throws Exception {
        String token = getUserToken(); // Regular user
        CategoryRequest req = new CategoryRequest();
        req.setName("Illegal-" + UUID.randomUUID());

        mockMvc.perform(post("/api/v1/categories")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Concurrency test for stock updates")
    void shouldHandleConcurrentSales() throws Exception {
        CategoryEntity cat = categoryRepository.save(CategoryEntity.builder().name("Stock").build());
        String token = getAdminToken();

        // Create product
        ProductRequest preq = ProductRequest.builder().name("Phone").price(BigDecimal.TEN).categoryId(cat.getId()).stockQuantity(10).build();
        MvcResult res = mockMvc.perform(post("/api/v1/products").header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(preq))).andReturn();
        Long pid = objectMapper.readValue(res.getResponse().getContentAsString(), ProductResponse.class).getId();

        await().atMost(15, TimeUnit.SECONDS).until(() -> inventoryRepository.existsByProductId(pid));

        int threads = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        CountDownLatch latch = new CountDownLatch(1);

        for (int i = 0; i < threads; i++) {
            executor.execute(() -> {
                try {
                    latch.await();
                    mockMvc.perform(post("/api/v1/inventory/sell/" + pid + "?quantity=1").header("Authorization", "Bearer " + token));
                } catch (Exception ignored) {}
            });
        }

        latch.countDown();
        executor.shutdown();
        executor.awaitTermination(20, TimeUnit.SECONDS);

        var inv = inventoryRepository.findByProductId(pid).orElseThrow();
        Assertions.assertEquals(0, inv.getAvailableQuantity());
    }

    @Test
    @DisplayName("Should return 403 Forbidden when a regular USER tries to delete a product")
    void shouldReturnForbiddenOnDelete() throws Exception {
        CategoryEntity cat = categoryRepository.save(CategoryEntity.builder().name("DeleteTest").build());

        ProductEntity product = productRepository.save(ProductEntity.builder()
                .name("Secure Item")
                .price(BigDecimal.ONE)
                .category(cat)
                .stockQuantity(1)
                .build());

        String userToken = getUserToken();

        mockMvc.perform(delete("/api/v1/products/" + product.getId())
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isForbidden());
    }

    // --- HELPER METHODS WITH UUID EMAILS (THE KEY TO SUCCESS) ---

    private String getAdminToken() throws Exception {
        return getToken("admin-" + UUID.randomUUID() + "@admin.com");
    }

    private String getUserToken() throws Exception {
        return getToken("user-" + UUID.randomUUID() + "@test.com");
    }

    private String getToken(String email) throws Exception {
        RegisterRequest req = new RegisterRequest(email, "password1234");
        MvcResult result = mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andReturn();
        return objectMapper.readValue(result.getResponse().getContentAsString(), AuthResponse.class).getAccessToken();
    }
}