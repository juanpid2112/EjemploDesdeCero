package com.prog4.EjemploDesdeCero.features.clases.services.impl.domain;

import org.springframework.stereotype.Service;

import com.prog4.EjemploDesdeCero.features.clases.dtos.request.ClasePatchRequestDto;
import com.prog4.EjemploDesdeCero.features.clases.dtos.response.ClaseResponseDto;
import com.prog4.EjemploDesdeCero.features.clases.models.Clase;
import com.prog4.EjemploDesdeCero.features.clases.repositories.IClaseRepository;
import com.prog4.EjemploDesdeCero.features.clases.services.interfaces.commons.IClaseFindByIdService;
import com.prog4.EjemploDesdeCero.features.clases.services.interfaces.domain.IClasePatchService;
import com.prog4.EjemploDesdeCero.features.clases.mappers.ClaseMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ClasePatchService implements IClasePatchService {
    
    private final IClaseRepository claseRepository;

    private final IClaseFindByIdService claseFindByIdService;

    @Override
    public ClaseResponseDto execute(Long id, ClasePatchRequestDto request) {
        Clase clase = claseFindByIdService.execute(id);
        
        return ClaseMapper.toResponseDto(
            claseRepository.save(
                ClaseMapper.toPatchModel(request, clase)
            )
        );
    }
}
