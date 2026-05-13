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

import com.prog4.EjemploDesdeCero.configs.exceptions.NotFoundException;
import com.prog4.EjemploDesdeCero.features.clases.dtos.request.ClasePatchRequestDto;
import com.prog4.EjemploDesdeCero.features.clases.dtos.response.ClaseResponseDto;
import com.prog4.EjemploDesdeCero.features.clases.models.Clase;
import com.prog4.EjemploDesdeCero.features.clases.repositories.IClaseRepository;
import com.prog4.EjemploDesdeCero.features.clases.services.interfaces.commons.IClaseFindByIdService;

@ExtendWith(MockitoExtension.class)
class ClasePatchServiceTest {

    @Mock
    private IClaseRepository claseRepository;

    @Mock
    private IClaseFindByIdService claseFindByIdService;

    @InjectMocks
    private ClasePatchService clasePatchService;

    @Test
    @DisplayName("execute aplica patch, guarda y devuelve DTO")
    void execute_happyPath() {
        Clase existing = Clase.builder()
                .id(3L)
                .name("Crossfit")
                .instructor("Pepe")
                .maxCapacity(20)
                .deleted(false)
                .build();
        when(claseFindByIdService.execute(3L)).thenReturn(existing);
        when(claseRepository.save(any(Clase.class))).thenAnswer(inv -> inv.getArgument(0));

        ClasePatchRequestDto patch = new ClasePatchRequestDto("Nuevo", 25);
        ClaseResponseDto result = clasePatchService.execute(3L, patch);

        assertThat(result.instructor()).isEqualTo("Nuevo");
        assertThat(result.maxCapacity()).isEqualTo(25);
        verify(claseFindByIdService).execute(3L);
        verify(claseRepository).save(any(Clase.class));
    }

    @Test
    @DisplayName("execute propaga NotFoundException cuando findById falla")
    void execute_notFoundFromFind() {
        when(claseFindByIdService.execute(404L)).thenThrow(new NotFoundException("Clase no encontrada"));

        ClasePatchRequestDto patch = new ClasePatchRequestDto("X", 10);

        assertThatThrownBy(() -> clasePatchService.execute(404L, patch))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("execute propaga error cuando save falla")
    void execute_propagatesWhenSaveFails() {
        Clase existing = Clase.builder()
                .id(7L)
                .name("Zumba")
                .instructor("Z")
                .maxCapacity(30)
                .deleted(false)
                .build();
        when(claseFindByIdService.execute(7L)).thenReturn(existing);
        when(claseRepository.save(any(Clase.class))).thenThrow(new RuntimeException("save error"));

        ClasePatchRequestDto patch = new ClasePatchRequestDto("Y", 12);

        assertThatThrownBy(() -> clasePatchService.execute(7L, patch))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("save error");
    }
}
