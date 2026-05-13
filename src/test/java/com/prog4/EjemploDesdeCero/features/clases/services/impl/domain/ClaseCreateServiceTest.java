package com.prog4.EjemploDesdeCero.features.clases.services.impl.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.prog4.EjemploDesdeCero.features.clases.dtos.request.ClaseCreateRequestDto;
import com.prog4.EjemploDesdeCero.features.clases.dtos.response.ClaseResponseDto;
import com.prog4.EjemploDesdeCero.features.clases.models.Clase;
import com.prog4.EjemploDesdeCero.features.clases.repositories.IClaseRepository;

@ExtendWith(MockitoExtension.class)
class ClaseCreateServiceTest {

    @Mock
    private IClaseRepository claseRepository;

    @InjectMocks
    private ClaseCreateService claseCreateService;

    @Test
    @DisplayName("execute persiste y devuelve el DTO mapeado en el camino feliz")
    void execute_savesOnce_andReturnsDto() {
        ClaseCreateRequestDto request = new ClaseCreateRequestDto("Yoga", "Ana", 15);
        Clase persisted = Clase.builder()
                .id(5L)
                .name("Yoga")
                .instructor("Ana")
                .maxCapacity(15)
                .deleted(false)
                .build();
        when(claseRepository.save(any(Clase.class))).thenReturn(persisted);

        ClaseResponseDto result = claseCreateService.execute(request);

        assertThat(result.id()).isEqualTo(5L);
        assertThat(result.name()).isEqualTo("Yoga");
        assertThat(result.instructor()).isEqualTo("Ana");
        assertThat(result.maxCapacity()).isEqualTo(15);
        verify(claseRepository).save(any(Clase.class));
    }

    @Test
    @DisplayName("execute propaga error cuando el repositorio falla al guardar")
    void execute_propagates_whenSaveFails() {
        ClaseCreateRequestDto request = new ClaseCreateRequestDto("Pilates", "Luis", 10);
        when(claseRepository.save(any(Clase.class))).thenThrow(new RuntimeException("fallo de persistencia simulado"));

        assertThatThrownBy(() -> claseCreateService.execute(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("fallo de persistencia");
    }
}
