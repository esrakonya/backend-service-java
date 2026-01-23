package org.esrakonya.backend.user;

import lombok.RequiredArgsConstructor;
import org.esrakonya.backend.common.exception.ResourceNotFoundException;
import org.esrakonya.backend.common.security.JwtService;
import org.esrakonya.backend.auth.dto.LoginRequest;
import org.esrakonya.backend.user.dto.UserRegistrationRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public void registerUser(UserRegistrationRequest request) {
        UserEntity user = UserEntity.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Set.of("ROLE_USER"))
                .build();

        userRepository.save(user);
    }

    public String login(LoginRequest request) {
        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return jwtService.generateToken(user.getEmail());
    }
}
