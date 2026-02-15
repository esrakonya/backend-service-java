package org.esrakonya.backend.service;

import org.esrakonya.backend.common.core.exception.ResourceNotFoundException;
import org.esrakonya.backend.product.domain.CategoryEntity;
import org.esrakonya.backend.product.domain.ProductEntity;
import org.esrakonya.backend.common.core.dto.product.ProductRequest;
import org.esrakonya.backend.common.core.dto.product.ProductResponse;
import org.esrakonya.backend.product.mapper.ProductMapper;
import org.esrakonya.backend.product.repository.CategoryRepository;
import org.esrakonya.backend.product.repository.ProductRepository;
import org.esrakonya.backend.product.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    @DisplayName("Ürün kaydedildiğinde kategori kontrol edilmeli ve Kafka mesajı gönderilmeli")
    void createProduct_Success() {
        // GIVEN
        ProductRequest request = ProductRequest.builder()
                .name("Test Product")
                .categoryId(1L)
                .price(BigDecimal.TEN)
                .stockQuantity(100)
                .build();

        CategoryEntity category = new CategoryEntity();
        category.setId(1L);
        category.setName("Electronics");

        ProductEntity productToSave = new ProductEntity();
        productToSave.setName("Test Product");

        ProductEntity savedProduct = new ProductEntity();
        savedProduct.setId(100L);
        savedProduct.setName("Test Product");
        savedProduct.setPrice(BigDecimal.TEN);
        savedProduct.setStockQuantity(100);

        ProductResponse expectedResponse = new ProductResponse();
        expectedResponse.setId(100L);

        // DOĞRU KULLANIM: when(MOCK_OBJESI.METOT_CAGRISI)
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productMapper.toEntity(any(ProductRequest.class))).thenReturn(productToSave);
        when(productRepository.saveAndFlush(any(ProductEntity.class))).thenReturn(savedProduct);
        when(productMapper.toResponse(any(ProductEntity.class))).thenReturn(expectedResponse);

        ProductResponse result = productService.createProduct(request);

        // THEN
        assertNotNull(result);
        assertEquals(100L, result.getId());
        verify(kafkaTemplate, times(1)).send(eq("product-created-topic"), any());
        verify(productRepository, times(1)).saveAndFlush(any());
    }

    @Test
    @DisplayName("Kategori bulunamazsa ResourceNotFoundException fırlatmalı")
    void createProduct_CategoryNotFound_ThrowsException() {
        ProductRequest request = ProductRequest.builder().categoryId(99L).build();
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.createProduct(request));

        verifyNoInteractions(productRepository);
        verifyNoInteractions(kafkaTemplate);
    }
}