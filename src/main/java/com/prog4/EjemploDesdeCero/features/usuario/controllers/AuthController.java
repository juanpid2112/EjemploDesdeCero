package com.prog4.EjemploDesdeCero.features.usuario.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prog4.EjemploDesdeCero.features.usuario.dtos.request.RegisterRequestDto;
import com.prog4.EjemploDesdeCero.features.usuario.dtos.request.LoginRequestDto;
import com.prog4.EjemploDesdeCero.features.usuario.dtos.response.AuthResponse;
import com.prog4.EjemploDesdeCero.features.usuario.services.interfaces.domain.IAuthenticationService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final IAuthenticationService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequestDto request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }
}   