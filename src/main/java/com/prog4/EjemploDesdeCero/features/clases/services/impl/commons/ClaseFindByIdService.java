package com.prog4.EjemploDesdeCero.features.clases.services.impl.commons;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.prog4.EjemploDesdeCero.configs.exceptions.NotFoundException;
import com.prog4.EjemploDesdeCero.features.clases.models.Clase;
import com.prog4.EjemploDesdeCero.features.clases.repositories.IClaseRepository;
import com.prog4.EjemploDesdeCero.features.clases.services.interfaces.commons.IClaseFindByIdService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ClaseFindByIdService implements IClaseFindByIdService {
    
    private final IClaseRepository claseRepository;

    @Override
    public Clase execute(Long id) {
        Optional<Clase> clase = claseRepository.findByIdAndDeletedFalse(id);
        if (clase.isEmpty()) {
            throw new NotFoundException("Clase no encontrada");
        }
        return clase.get();
    }
}
