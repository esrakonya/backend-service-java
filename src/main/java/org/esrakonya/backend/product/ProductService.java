package org.esrakonya.backend.product;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.esrakonya.backend.common.exception.ResourceNotFoundException;
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

    public ProductResponse getProductById(Long id) {
        ProductEntity entity = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        return ProductResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        // Check if product exists
        ProductEntity existingProduct = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cannot update. Product not found with id: " + id));

        // Update fields
        existingProduct.setName(request.getName());
        existingProduct.setDescription(request.getDescription());
        existingProduct.setPrice(request.getPrice());

        // Save and map back
        ProductEntity updatedProduct = productRepository.save(existingProduct);
        return ProductResponse.builder()
                .id(updatedProduct.getId())
                .name(updatedProduct.getName())
                .description(updatedProduct.getDescription())
                .price(updatedProduct.getPrice())
                .createdAt(updatedProduct.getCreatedAt())
                .build();
    }


    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cannot delete. Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }
}
