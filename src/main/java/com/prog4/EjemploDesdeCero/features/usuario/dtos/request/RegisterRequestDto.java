package com.prog4.EjemploDesdeCero.features.usuario.dtos.request;

import com.prog4.EjemploDesdeCero.features.usuario.models.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;

public record RegisterRequestDto (
    @NotBlank String username,
    @NotBlank @Size(min = 6) String password,
    @NotNull Role role
) {}
