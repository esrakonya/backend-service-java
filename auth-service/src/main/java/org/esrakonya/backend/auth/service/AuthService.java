package org.esrakonya.backend.auth.service;

import org.esrakonya.backend.common.dto.AuthResponse;
import org.esrakonya.backend.common.dto.LoginRequest;
import org.esrakonya.backend.common.dto.RegisterRequest;

public interface AuthService {

    AuthResponse login(LoginRequest request);

    AuthResponse register(RegisterRequest request);
}
