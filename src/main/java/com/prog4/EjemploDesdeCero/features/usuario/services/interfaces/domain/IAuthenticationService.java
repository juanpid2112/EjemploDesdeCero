package com.prog4.EjemploDesdeCero.features.usuario.services.interfaces.domain;

import com.prog4.EjemploDesdeCero.features.usuario.dtos.request.RegisterRequestDto;
import com.prog4.EjemploDesdeCero.features.usuario.dtos.request.LoginRequestDto;
import com.prog4.EjemploDesdeCero.features.usuario.dtos.response.AuthResponse;

public interface IAuthenticationService {
    AuthResponse register (RegisterRequestDto request);
    
    AuthResponse authenticate (LoginRequestDto request);
}
