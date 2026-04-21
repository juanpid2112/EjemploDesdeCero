package com.prog4.EjemploDesdeCero.features.clases.services.interfaces.domain;

import com.prog4.EjemploDesdeCero.features.clases.dtos.request.ClaseCreateRequestDto;
import com.prog4.EjemploDesdeCero.features.clases.dtos.response.ClaseResponseDto;

public interface IClaseCreateService {

    ClaseResponseDto execute(ClaseCreateRequestDto request);

}
