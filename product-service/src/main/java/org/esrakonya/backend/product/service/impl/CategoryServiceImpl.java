package org.esrakonya.backend.product.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.esrakonya.backend.product.domain.CategoryEntity;
import org.esrakonya.backend.product.dto.CategoryRequest;
import org.esrakonya.backend.product.dto.CategoryResponse;
import org.esrakonya.backend.product.mapper.CategoryMapper;
import org.esrakonya.backend.product.repository.CategoryRepository;
import org.esrakonya.backend.product.service.CategoryService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    @CacheEvict(value = "categories", allEntries = true)
    public CategoryResponse createCategory(CategoryRequest request) {
        log.info("Creating new category: {}. Evicting cache.", request.getName());
        CategoryEntity entity = categoryMapper.toEntity(request);
        return categoryMapper.toResponse(categoryRepository.save(entity));
    }

    @Override
    @Cacheable(value = "categories")
    public List<CategoryResponse> getAllCategories() {
        log.info("### CACHE MISS: Fetching categories from Database... ###");
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toResponse)
                .toList();
    }
}
