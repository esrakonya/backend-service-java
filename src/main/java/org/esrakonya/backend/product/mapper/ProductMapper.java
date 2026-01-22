package org.esrakonya.backend.product.mapper;

import org.esrakonya.backend.product.ProductEntity;
import org.esrakonya.backend.product.dto.ProductRequest;
import org.esrakonya.backend.product.dto.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    // Converts Request DTO to Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    ProductEntity toEntity(ProductRequest request);

    // Converts Entity to Response DTO
    ProductResponse toResponse(ProductEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromRequest(ProductRequest request, @MappingTarget ProductEntity entity);
}
