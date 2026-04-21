package com.prog4.EjemploDesdeCero.features.clases.services.interfaces.domain;

import java.util.List;

import com.prog4.EjemploDesdeCero.features.clases.dtos.response.ClaseResponseDto;

public interface IClaseListService {
    
    List<ClaseResponseDto> execute();
}
