package com.prog4.EjemploDesdeCero.features.clases.services.impl.commons;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.prog4.EjemploDesdeCero.configs.exceptions.NotFoundException;
import com.prog4.EjemploDesdeCero.features.clases.models.Clase;
import com.prog4.EjemploDesdeCero.features.clases.repositories.IClaseRepository;

@ExtendWith(MockitoExtension.class)
class ClaseFindByIdServiceTest {

    @Mock
    private IClaseRepository claseRepository;

    @InjectMocks
    private ClaseFindByIdService claseFindByIdService;

    @Test
    @DisplayName("execute devuelve la clase cuando existe y no está borrada")
    void execute_returnsClase_whenFoundAndNotDeleted() {
        Clase expected = Clase.builder()
                .id(10L)
                .name("Spinning")
                .instructor("María")
                .maxCapacity(20)
                .deleted(false)
                .build();
        when(claseRepository.findByIdAndDeletedFalse(10L)).thenReturn(Optional.of(expected));

        Clase result = claseFindByIdService.execute(10L);

        assertThat(result).isSameAs(expected);
        verify(claseRepository).findByIdAndDeletedFalse(10L);
    }

    @Test
    @DisplayName("execute lanza NotFoundException cuando no hay clase activa")
    void execute_throwsNotFound_whenMissing() {
        when(claseRepository.findByIdAndDeletedFalse(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> claseFindByIdService.execute(99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Clase no encontrada");
    }

    @Test
    @DisplayName("execute con id null se traduce en no encontrada si el repositorio devuelve vacío")
    void execute_treatsNullAsNotFound_whenRepositoryReturnsEmpty() {
        when(claseRepository.findByIdAndDeletedFalse(null)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> claseFindByIdService.execute(null))
                .isInstanceOf(NotFoundException.class);
    }
}
