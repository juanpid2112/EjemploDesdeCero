package com.prog4.EjemploDesdeCero.features.usuario.dtos.response;

public record AuthResponse(
    String token,
    String type, 
    String username
) {
    public AuthResponse(String token) {
        this(token, "Bearer", null);
    }
    
}
