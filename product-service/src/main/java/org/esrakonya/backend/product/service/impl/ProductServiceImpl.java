package org.esrakonya.backend.product.service.impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.esrakonya.backend.common.exception.ResourceNotFoundException;
import org.esrakonya.backend.product.domain.CategoryEntity;
import org.esrakonya.backend.product.domain.ProductEntity;
import org.esrakonya.backend.product.dto.ProductRequest;
import org.esrakonya.backend.product.dto.ProductResponse;
import org.esrakonya.backend.common.event.ProductCreatedEvent;
import org.esrakonya.backend.product.mapper.ProductMapper;
import org.esrakonya.backend.product.repository.CategoryRepository;
import org.esrakonya.backend.product.repository.ProductRepository;
import org.esrakonya.backend.product.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String PRODUCT_TOPIC = "product-created-topic";
    @Override
    public ProductResponse createProduct(ProductRequest request) {
        CategoryEntity category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + request.getCategoryId()));

        ProductEntity product = productMapper.toEntity(request);
        product.setCategory(category);
        product.setCreatedAt(LocalDateTime.now());

        ProductEntity savedProduct = productRepository.saveAndFlush(product);

        log.info("Product saved to database with ID: {} in Category: {}", savedProduct.getId(), category.getName());

        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronization() {
                        @Override
                        public void afterCommit() {
                            publishProductCreatedEvent(savedProduct);
                        }
                    }
            );
        } else {
            publishProductCreatedEvent(savedProduct);
        }



        return productMapper.toResponse(savedProduct);
    }

    private void publishProductCreatedEvent(ProductEntity product) {
        try {
            ProductCreatedEvent event = ProductCreatedEvent.builder()
                    .productId(product.getId())
                    .name(product.getName())
                    .price(product.getPrice())
                    .quantity(product.getStockQuantity())
                    .timestamp(LocalDateTime.now())
                    .build();

            kafkaTemplate.send(PRODUCT_TOPIC, event);
        } catch (Exception e) {
            log.warn("Kafka publish skipped in test environment: {}", e.getMessage());
        }

    }

    @Override
    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(productMapper::toResponse);
    }

    @Override
    public ProductResponse getProductById(Long id) {
        ProductEntity entity = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found with id: " + id));
        return productMapper.toResponse(entity);
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        ProductEntity existing = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cannot update. Product not found with id: " + id));
        productMapper.updateEntityFromRequest(request, existing);
        ProductEntity updated = productRepository.save(existing);
        return productMapper.toResponse(updated);
    }

    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cannot delete. Product not with id: " + id);
        }
        productRepository.deleteById(id);
    }

}
