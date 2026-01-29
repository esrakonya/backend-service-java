package org.esrakonya.backend.product.service;

import org.esrakonya.backend.product.dto.CategoryRequest;
import org.esrakonya.backend.product.dto.CategoryResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse createCategory(CategoryRequest request);
    List<CategoryResponse> getAllCategories();
}
