package com.prog4.EjemploDesdeCero.features.clases.services.impl.domain;

import java.util.List;

import org.springframework.stereotype.Service;

import com.prog4.EjemploDesdeCero.features.clases.dtos.response.ClaseResponseDto;
import com.prog4.EjemploDesdeCero.features.clases.repositories.IClaseRepository;
import com.prog4.EjemploDesdeCero.features.clases.mappers.ClaseMapper;
import com.prog4.EjemploDesdeCero.features.clases.services.interfaces.domain.IClaseListService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ClaseListService implements IClaseListService {
    
    private final IClaseRepository claseRepository;
    
    @Override
    public List<ClaseResponseDto> execute() {
        return ClaseMapper.toResponseDtoList(claseRepository.findByDeletedFalse());
    }
    
}
