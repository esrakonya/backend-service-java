package org.esrakonya.backend.auth.mapper;

import org.esrakonya.backend.common.dto.AuthResponse;
import org.esrakonya.backend.common.dto.RegisterRequest;
import org.esrakonya.backend.user.domain.Role;
import org.esrakonya.backend.user.domain.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AuthMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", ignore = true)
    UserEntity toEntity(RegisterRequest request);

    @Mapping(target = "accessToken", source = "token")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "roles", source = "user.roles")
    AuthResponse toResponse(UserEntity user, String token);

    // MapStruct needs to know how to turn Set<Role> into Set<String>
    default Set<String> mapRoles(Set<Role> roles) {
        if (roles == null) return null;
        return roles.stream()
                .map(Enum::name)
                .collect(Collectors.toSet());
    }
}
