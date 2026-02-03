package org.esrakonya.backend.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.esrakonya.backend.user.domain.Role;

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
