package org.esrakonya.backend.auth.mapper;

import org.esrakonya.backend.auth.dto.RegisterRequest;
import org.esrakonya.backend.user.domain.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AuthMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", ignore = true)
    UserEntity toEntity(RegisterRequest request);
}
