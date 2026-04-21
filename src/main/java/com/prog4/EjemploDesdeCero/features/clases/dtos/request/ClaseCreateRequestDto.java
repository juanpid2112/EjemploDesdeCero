package com.prog4.EjemploDesdeCero.features.clases.dtos.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ClaseCreateRequestDto (
    @NotBlank String name,
    @NotBlank String instructor,
    @Min(5) @Max(50) Integer maxCapacity
) {
    
}
