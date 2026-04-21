package com.prog4.EjemploDesdeCero.features.clases.repositories;

import org.springframework.data.repository.CrudRepository;
import com.prog4.EjemploDesdeCero.features.clases.models.Clase;

public interface IClaseRepository extends CrudRepository<Clase, Long> {
    
}
