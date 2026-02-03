package org.esrakonya.backend.user.mapper;

import lombok.AllArgsConstructor;
import org.esrakonya.backend.user.domain.Role;
import org.esrakonya.backend.user.domain.UserEntity;
import org.esrakonya.backend.user.dto.UserRegistrationRequest;
import org.esrakonya.backend.user.dto.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(target = "password", ignore = true)
     UserEntity toEntity(UserRegistrationRequest request);


    UserResponse toResponse(UserEntity entity);
}
