package com.prog4.EjemploDesdeCero.features.clases.services.interfaces.commons;

import com.prog4.EjemploDesdeCero.features.clases.models.Clase;

public interface IClaseFindByIdService {
    
    Clase execute(Long id);
}
