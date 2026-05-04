package com.prog4.EjemploDesdeCero.features.usuario.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prog4.EjemploDesdeCero.features.usuario.dtos.request.LoginRequestDto;
import com.prog4.EjemploDesdeCero.features.usuario.dtos.request.RegisterRequestDto;
import com.prog4.EjemploDesdeCero.features.usuario.dtos.response.AuthResponse;
import com.prog4.EjemploDesdeCero.features.usuario.services.interfaces.domain.IAuthenticationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Tag(name = "Autenticación", description = "Registro y login. Endpoints públicos (sin JWT).")
@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final IAuthenticationService authService;

    @Operation(summary = "Registrar usuario", description = "Crea un usuario y devuelve un JWT.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Usuario registrado; cuerpo con token"),
        @ApiResponse(responseCode = "400", description = "Validación fallida (RFC 7807)")
    })
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    @Operation(summary = "Iniciar sesión", description = "Valida credenciales y devuelve un JWT.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Autenticación correcta; cuerpo con token"),
        @ApiResponse(responseCode = "400", description = "Validación fallida (RFC 7807)"),
        @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequestDto request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }
}
