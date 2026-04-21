package com.prog4.EjemploDesdeCero.features.clases.mappers;

import java.util.List;

import com.prog4.EjemploDesdeCero.features.clases.dtos.request.ClaseCreateRequestDto;
import com.prog4.EjemploDesdeCero.features.clases.models.Clase;
import com.prog4.EjemploDesdeCero.features.clases.dtos.response.ClaseResponseDto;

public class ClaseMapper {
    
    public static Clase toModel (ClaseCreateRequestDto request) {
        return Clase.builder()
                .name(request.name())
                .instructor(request.instructor())
                .maxCapacity(request.maxCapacity())
                .build();
    }

    public static ClaseResponseDto toResponseDto (Clase model) {
        return new ClaseResponseDto(
            model.getId(),
            model.getName(),
            model.getInstructor(),
            model.getMaxCapacity()
        );
    }

    public static List<ClaseResponseDto> toResponseDtoList (List<Clase> models) {
        return models.stream()
                .map(ClaseMapper::toResponseDto)
                .toList();
    }
}
