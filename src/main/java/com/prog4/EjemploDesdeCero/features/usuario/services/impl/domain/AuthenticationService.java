package com.prog4.EjemploDesdeCero.features.usuario.services.impl.domain;

import org.springframework.stereotype.Service;

import com.prog4.EjemploDesdeCero.features.usuario.dtos.request.RegisterRequestDto;
import com.prog4.EjemploDesdeCero.features.usuario.dtos.request.LoginRequestDto;
import com.prog4.EjemploDesdeCero.features.usuario.dtos.response.AuthResponse;
import com.prog4.EjemploDesdeCero.features.usuario.models.User;
import com.prog4.EjemploDesdeCero.features.usuario.repositories.IUserRepository;
import com.prog4.EjemploDesdeCero.features.usuario.services.interfaces.domain.IAuthenticationService;
import com.prog4.EjemploDesdeCero.features.usuario.services.interfaces.domain.IJwtService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthenticationService implements IAuthenticationService {

    private final IUserRepository userRepository;

    private final IJwtService jwtService;

    // PasswordEncoder se encarga de encriptar la password
    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;
    // AuthenticationManager se encarga de autenticar al usuario
    
    @Override
    public AuthResponse register(RegisterRequestDto request) {
        User user = new User();
        user.setUsername(request.username());
        // Encriptamos la password antes de guardar
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(request.role());
        
        userRepository.save(user);
        
        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }

    @Override
    public AuthResponse authenticate(LoginRequestDto request) {
        // El AuthenticationManager se encarga de validar credenciales
        authenticationManager.authenticate(
            // Creamos un token de autenticacion con el username y password
            new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );
        
        User user = userRepository.findByUsername(request.username())
                // Si no se encuentra el usuario, lanzamos una excepcion
                .orElseThrow();
        
        // Generamos un token para el usuario
        String token = jwtService.generateToken(user);
        // Devolvemos el token en un objeto AuthResponse
        return new AuthResponse(token);
    }
    
}
