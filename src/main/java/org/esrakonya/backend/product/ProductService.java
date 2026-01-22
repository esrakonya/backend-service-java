package org.esrakonya.backend.product;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.esrakonya.backend.product.dto.ProductRequest;
import org.esrakonya.backend.product.dto.ProductResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        // Map DTO to Entity
        ProductEntity productEntity = ProductEntity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .build();

        // Save to Database
        ProductEntity savedProduct = productRepository.save(productEntity);

        // Map Entity back to Response DTO
        return ProductResponse.builder()
                .id(savedProduct.getId())
                .name(savedProduct.getName())
                .description(savedProduct.getDescription())
                .price(savedProduct.getPrice())
                .createdAt(savedProduct.getCreatedAt())
                .build();
    }
}
