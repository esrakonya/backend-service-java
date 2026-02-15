package org.esrakonya.backend.product.mapper;

import org.esrakonya.backend.product.domain.ProductEntity;
import org.esrakonya.backend.common.core.dto.product.ProductRequest;
import org.esrakonya.backend.common.core.dto.product.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    // Converts Request DTO to Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "category", ignore = true)
    ProductEntity toEntity(ProductRequest request);

    // Converts Entity to Response DTO
    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "categoryId", source = "category.id")
    ProductResponse toResponse(ProductEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "category", ignore = true)
    void updateEntityFromRequest(ProductRequest request, @MappingTarget ProductEntity entity);

}
