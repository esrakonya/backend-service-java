package org.esrakonya.backend.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.esrakonya.backend.BaseIntegrationTest;
import org.esrakonya.backend.common.web.security.service.TokenService;
import org.esrakonya.backend.product.domain.CategoryEntity;
import org.esrakonya.backend.product.domain.ProductEntity;
import org.esrakonya.backend.product.dto.CategoryRequest;
import org.esrakonya.backend.common.core.dto.product.ProductRequest;
import org.esrakonya.backend.product.repository.CategoryRepository;
import org.esrakonya.backend.product.repository.ProductRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
class ProductIntegrationTest extends BaseIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private ProductRepository productRepository;

    @Autowired private TokenService tokenService;

    @AfterEach
    void cleanUp() {
        // Only clean what belongs to THIS service
        productRepository.deleteAllInBatch();
        categoryRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("Should create product and verify API response")
    void shouldCreateProductWithCategory() throws Exception {
        CategoryEntity cat = categoryRepository.save(CategoryEntity.builder().name("Electronics-" + UUID.randomUUID()).build());
        String token = getAdminToken();

        ProductRequest req = ProductRequest.builder()
                .name("Test Product")
                .price(new BigDecimal("100.00"))
                .categoryId(cat.getId())
                .stockQuantity(50)
                .build();

        mockMvc.perform(post("/api/v1/products")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Test Product"))
                .andExpect(jsonPath("$.categoryName").value(cat.getName()));
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
    @DisplayName("REGULAR USER should be forbidden from creating a category")
    void userShouldBeForbidden() throws Exception {
        String token = getUserToken();
        CategoryRequest req = new CategoryRequest();
        req.setName("Illegal-" + UUID.randomUUID());

        mockMvc.perform(post("/api/v1/categories")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should return 403 Forbidden when a regular USER tries to delete a product")
    void shouldReturnForbiddenOnDelete() throws Exception {
        CategoryEntity cat = categoryRepository.save(CategoryEntity.builder().name("DeleteTest").build());
        ProductEntity product = productRepository.save(ProductEntity.builder()
                .name("Secure Item").price(BigDecimal.ONE).category(cat).stockQuantity(1).build());

        String userToken = getUserToken();

        mockMvc.perform(delete("/api/v1/products/" + product.getId())
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isForbidden());
    }

    // --- HELPER METHODS ---

    private String getAdminToken() throws Exception {
        var admin = org.springframework.security.core.userdetails.User.builder()
                .username("admin@test.com")
                .password("pass")
                .authorities("ROLE_ADMIN")
                .build();
        return tokenService.generateToken(admin);
    }

    private String getUserToken() throws Exception {
        var user = org.springframework.security.core.userdetails.User.builder()
                .username("user@test.com")
                .password("pass")
                .authorities("ROLE_USER")
                .build();
        return tokenService.generateToken(user);
    }
}