package com.prog4.EjemploDesdeCero.features.clases.services.interfaces.domain;

import com.prog4.EjemploDesdeCero.features.clases.dtos.request.ClasePatchRequestDto;
import com.prog4.EjemploDesdeCero.features.clases.dtos.response.ClaseResponseDto;

public interface IClasePatchService {
    
    ClaseResponseDto execute(Long id, ClasePatchRequestDto request);
}
