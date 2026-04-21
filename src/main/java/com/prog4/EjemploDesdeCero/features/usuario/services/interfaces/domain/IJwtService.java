package com.prog4.EjemploDesdeCero.features.usuario.services.interfaces.domain;

import org.springframework.security.core.userdetails.UserDetails;

public interface IJwtService {
    String generateToken(UserDetails user);

    String extractUsername(String token);
    
    boolean isTokenValid(String token, UserDetails user);
}
