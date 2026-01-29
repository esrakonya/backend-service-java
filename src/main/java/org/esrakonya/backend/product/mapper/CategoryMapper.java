package org.esrakonya.backend.product.mapper;

import org.esrakonya.backend.product.domain.CategoryEntity;
import org.esrakonya.backend.product.dto.CategoryRequest;
import org.esrakonya.backend.product.dto.CategoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "products", ignore = true)
    CategoryEntity toEntity(CategoryRequest request);

    CategoryResponse toResponse(CategoryEntity entity);
}
