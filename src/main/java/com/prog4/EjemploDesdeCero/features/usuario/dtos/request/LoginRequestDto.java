package com.prog4.EjemploDesdeCero.features.usuario.dtos.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(
    @NotBlank String username,
    @NotBlank String password
) {}
