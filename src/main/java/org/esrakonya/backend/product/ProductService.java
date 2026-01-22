package org.esrakonya.backend.product;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.esrakonya.backend.product.dto.ProductRequest;
import org.esrakonya.backend.product.dto.ProductResponse;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(product -> ProductResponse.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .description(product.getDescription())
                        .price(product.getPrice())
                        .createdAt(product.getCreatedAt())
                        .build())
                .toList();
    }
}
