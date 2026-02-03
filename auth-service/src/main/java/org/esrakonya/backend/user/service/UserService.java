package org.esrakonya.backend.user.service;

import org.esrakonya.backend.user.domain.UserEntity;

import java.util.Optional;
import java.util.Set;

public interface UserService {

    UserEntity createUser(UserEntity user);

    Optional<UserEntity> findByEmail(String email);

    boolean existsByEmail(String email);

}
