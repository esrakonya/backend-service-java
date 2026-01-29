package org.esrakonya.backend.auth.service;

import org.esrakonya.backend.auth.dto.AuthResponse;
import org.esrakonya.backend.auth.dto.LoginRequest;
import org.esrakonya.backend.auth.dto.RegisterRequest;

public interface AuthService {

    AuthResponse login(LoginRequest request);

    AuthResponse register(RegisterRequest request);
}
