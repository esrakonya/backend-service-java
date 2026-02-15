package org.esrakonya.backend.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.esrakonya.backend.common.core.enums.Role;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Builder
@AllArgsConstructor
public class UserResponse {

    private Long id;
    private String email;
    private Set<Role> roles;
    private LocalDateTime createdAt;
}
