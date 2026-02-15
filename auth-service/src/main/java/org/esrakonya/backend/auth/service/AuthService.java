package org.esrakonya.backend.auth.service;

import org.esrakonya.backend.common.core.dto.auth.AuthResponse;
import org.esrakonya.backend.common.core.dto.auth.LoginRequest;
import org.esrakonya.backend.common.core.dto.auth.RegisterRequest;
public interface AuthService {

    AuthResponse login(LoginRequest request);

    AuthResponse register(RegisterRequest request);
}
