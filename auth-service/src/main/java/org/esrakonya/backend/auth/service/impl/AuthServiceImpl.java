package org.esrakonya.backend.auth.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.esrakonya.backend.auth.mapper.AuthMapper;
import org.esrakonya.backend.auth.service.AuthService;
import org.esrakonya.backend.common.core.dto.auth.AuthResponse;
import org.esrakonya.backend.common.core.dto.auth.LoginRequest;
import org.esrakonya.backend.common.core.dto.auth.RegisterRequest;
import org.esrakonya.backend.common.core.enums.Role;
import org.esrakonya.backend.auth.domain.UserEntity;
import org.esrakonya.backend.auth.service.UserService;
import org.esrakonya.backend.common.core.exception.InvalidCredentialsException;
import org.esrakonya.backend.common.core.exception.ResourceAlreadyExistsException;
import org.esrakonya.backend.common.web.security.service.TokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final AuthMapper authMapper;

    @Override
    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (Exception e) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        UserEntity user = userService.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("User not found after authentication"));

        String token = tokenService.generateToken(user);

        return AuthResponse.builder()
                .accessToken(token)
                .email(user.getEmail())
                .roles(user.getRoles().stream()
                        .map(Enum::name)
                        .collect(Collectors.toSet()))
                .build();
    }

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userService.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("Email already exists: " + request.getEmail());
        }

        UserEntity userEntity = authMapper.toEntity(request);
        userEntity.setPassword(passwordEncoder.encode(request.getPassword()));

        // TODO: Move this to a proper Admin Management system in Phase 23
        if (request.getEmail().endsWith("@admin.com")) {
            userEntity.setRoles(Set.of(Role.ADMIN, Role.USER));
            log.warn("Privileged account created for: {}", request.getEmail());
        } else {
            userEntity.setRoles(Set.of(Role.USER));
        }

        UserEntity savedUser = userService.createUser(userEntity);

        String token = tokenService.generateToken(savedUser);

        return authMapper.toResponse(savedUser, token);
    }
}
