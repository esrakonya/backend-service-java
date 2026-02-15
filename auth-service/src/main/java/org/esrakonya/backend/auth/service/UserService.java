package org.esrakonya.backend.auth.service;

import org.esrakonya.backend.auth.domain.UserEntity;

import java.util.Optional;

public interface UserService {

    UserEntity createUser(UserEntity user);

    Optional<UserEntity> findByEmail(String email);

    boolean existsByEmail(String email);

}
