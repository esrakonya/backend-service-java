package org.esrakonya.backend.auth.mapper;

import org.esrakonya.backend.auth.domain.UserEntity;
import org.esrakonya.backend.auth.dto.UserRegistrationRequest;
import org.esrakonya.backend.auth.dto.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(target = "password", ignore = true)
     UserEntity toEntity(UserRegistrationRequest request);


    UserResponse toResponse(UserEntity entity);
}
