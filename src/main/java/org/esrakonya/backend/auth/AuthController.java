package org.esrakonya.backend.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.esrakonya.backend.auth.dto.AuthResponse;
import org.esrakonya.backend.auth.dto.LoginRequest;
import org.esrakonya.backend.user.UserService;
import org.esrakonya.backend.user.dto.UserRegistrationRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody UserRegistrationRequest request) {
        userService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        String token = userService.login(request);
        return ResponseEntity.ok(AuthResponse.builder().token(token).build());
    }
}
