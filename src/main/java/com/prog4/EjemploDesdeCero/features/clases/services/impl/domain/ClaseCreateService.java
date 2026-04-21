package com.prog4.EjemploDesdeCero.features.clases.services.impl.domain;

import org.springframework.stereotype.Service;

import com.prog4.EjemploDesdeCero.features.clases.dtos.request.ClaseCreateRequestDto;
import com.prog4.EjemploDesdeCero.features.clases.dtos.response.ClaseResponseDto;
import com.prog4.EjemploDesdeCero.features.clases.services.interfaces.domain.IClaseCreateService;
import com.prog4.EjemploDesdeCero.features.clases.repositories.IClaseRepository;
import com.prog4.EjemploDesdeCero.features.clases.mappers.ClaseMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ClaseCreateService implements IClaseCreateService {
    
    private final IClaseRepository claseRepository;

    @Override
    public ClaseResponseDto execute(ClaseCreateRequestDto request) {
        return ClaseMapper.toResponseDto(claseRepository.save(ClaseMapper.toModel(request)));
    }
    
}
