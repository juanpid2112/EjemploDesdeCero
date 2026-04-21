package com.prog4.EjemploDesdeCero.features.clases.dtos.response;

import java.time.LocalDateTime;


public record ClaseResponseDto(
    Long id,
    String name,
    String instructor,
    Integer maxCapacity,
    LocalDateTime startTime
) {
    
}
