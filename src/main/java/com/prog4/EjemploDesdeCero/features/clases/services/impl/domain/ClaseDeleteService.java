package com.prog4.EjemploDesdeCero.features.clases.services.impl.domain;

import org.springframework.stereotype.Service;

import com.prog4.EjemploDesdeCero.features.clases.services.interfaces.domain.IClaseDeleteService;
import com.prog4.EjemploDesdeCero.features.clases.repositories.IClaseRepository;
import com.prog4.EjemploDesdeCero.features.clases.services.interfaces.commons.IClaseFindByIdService;
import com.prog4.EjemploDesdeCero.features.clases.models.Clase;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ClaseDeleteService implements IClaseDeleteService {
    
    private final IClaseRepository claseRepository;

    private final IClaseFindByIdService claseFindByIdService;
    
    @Override
    public void execute(Long id) {
        Clase clase = claseFindByIdService.execute(id);

        clase.setDeleted(true);

        claseRepository.save(clase);
    }
    
}
