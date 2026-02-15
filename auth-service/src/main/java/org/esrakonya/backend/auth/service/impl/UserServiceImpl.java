package org.esrakonya.backend.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.esrakonya.backend.auth.domain.UserEntity;
import org.esrakonya.backend.auth.repository.UserRepository;
import org.esrakonya.backend.auth.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserEntity createUser(UserEntity user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<UserEntity> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
