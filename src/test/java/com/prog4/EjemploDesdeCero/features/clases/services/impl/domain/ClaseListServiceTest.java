package com.prog4.EjemploDesdeCero.features.clases.services.impl.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.prog4.EjemploDesdeCero.features.clases.dtos.response.ClaseResponseDto;
import com.prog4.EjemploDesdeCero.features.clases.models.Clase;
import com.prog4.EjemploDesdeCero.features.clases.repositories.IClaseRepository;

@ExtendWith(MockitoExtension.class)
class ClaseListServiceTest {

    @Mock
    private IClaseRepository claseRepository;

    @InjectMocks
    private ClaseListService claseListService;

    @Test
    @DisplayName("execute devuelve la lista mapeada cuando hay elementos")
    void execute_returnsMappedList_whenNonEmpty() {
        List<Clase> models = List.of(
                Clase.builder().id(1L).name("A").instructor("I1").maxCapacity(10).deleted(false).build(),
                Clase.builder().id(2L).name("B").instructor("I2").maxCapacity(12).deleted(false).build());
        when(claseRepository.findByDeletedFalse()).thenReturn(models);

        List<ClaseResponseDto> result = claseListService.execute();

        assertThat(result).hasSize(2);
        assertThat(result.getFirst().name()).isEqualTo("A");
        verify(claseRepository).findByDeletedFalse();
    }

    @Test
    @DisplayName("execute devuelve lista vacía cuando no hay clases activas")
    void execute_returnsEmptyList() {
        when(claseRepository.findByDeletedFalse()).thenReturn(List.of());

        List<ClaseResponseDto> result = claseListService.execute();

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("execute propaga excepción cuando el repositorio falla")
    void execute_propagates_whenRepositoryFails() {
        when(claseRepository.findByDeletedFalse()).thenThrow(new RuntimeException("db caída"));

        assertThatThrownBy(() -> claseListService.execute())
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("db caída");
    }
}
