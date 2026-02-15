package org.esrakonya.backend.common.web.security.service;


import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface TokenService {

    String generateToken(UserDetails userDetails);

    String extractSubject(String token);

    boolean isTokenValid(String token);

    List<String> extractRoles(String token);
}
