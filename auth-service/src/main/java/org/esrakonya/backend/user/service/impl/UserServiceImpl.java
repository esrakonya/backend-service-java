package org.esrakonya.backend.user.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.esrakonya.backend.common.exception.ResourceAlreadyExistsException;
import org.esrakonya.backend.user.domain.UserEntity;
import org.esrakonya.backend.user.dto.UserRegistrationRequest;
import org.esrakonya.backend.user.mapper.UserMapper;
import org.esrakonya.backend.user.repository.UserRepository;
import org.esrakonya.backend.user.service.UserService;
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
