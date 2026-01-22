package org.esrakonya.backend.product;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.esrakonya.backend.common.exception.ResourceNotFoundException;
import org.esrakonya.backend.product.dto.ProductRequest;
import org.esrakonya.backend.product.dto.ProductResponse;
import org.esrakonya.backend.product.mapper.ProductMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        ProductEntity entity = productMapper.toEntity(request);
        ProductEntity saved = productRepository.save(entity);
        return productMapper.toResponse(saved);
    }

    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(productMapper::toResponse);
    }

    public ProductResponse getProductById(Long id) {
        ProductEntity entity = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        return productMapper.toResponse(entity);
    }

    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        // Check if product exists
        ProductEntity existingProduct = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cannot update. Product not found with id: " + id));

        // Update fields
        productMapper.updateEntityFromRequest(request, existingProduct);

        // Save and map back
        ProductEntity updatedProduct = productRepository.save(existingProduct);
        return productMapper.toResponse(updatedProduct);
    }


    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cannot delete. Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }
}
