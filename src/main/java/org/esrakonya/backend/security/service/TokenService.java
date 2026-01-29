package org.esrakonya.backend.security.service;


import org.springframework.security.core.userdetails.UserDetails;

public interface TokenService {

    String generateToken(UserDetails userDetails);

    String extractSubject(String token);

    boolean isTokenValid(String token, UserDetails userDetails);
}
