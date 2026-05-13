package com.prog4.EjemploDesdeCero.features.clases.services.impl.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.prog4.EjemploDesdeCero.configs.exceptions.NotFoundException;
import com.prog4.EjemploDesdeCero.features.clases.models.Clase;
import com.prog4.EjemploDesdeCero.features.clases.repositories.IClaseRepository;
import com.prog4.EjemploDesdeCero.features.clases.services.interfaces.commons.IClaseFindByIdService;

@ExtendWith(MockitoExtension.class)
class ClaseDeleteServiceTest {

    @Mock
    private IClaseRepository claseRepository;

    @Mock
    private IClaseFindByIdService claseFindByIdService;

    @InjectMocks
    private ClaseDeleteService claseDeleteService;

    @Test
    @DisplayName("execute marca deleted y persiste la entidad")
    void execute_softDeletes_andSaves() {
        Clase existing = Clase.builder()
                .id(2L)
                .name("HIIT")
                .instructor("Coach")
                .maxCapacity(15)
                .deleted(false)
                .build();
        when(claseFindByIdService.execute(2L)).thenReturn(existing);
        when(claseRepository.save(any(Clase.class))).thenAnswer(inv -> inv.getArgument(0));

        claseDeleteService.execute(2L);

        ArgumentCaptor<Clase> captor = ArgumentCaptor.forClass(Clase.class);
        verify(claseRepository).save(captor.capture());
        assertThat(captor.getValue().isDeleted()).isTrue();
    }

    @Test
    @DisplayName("execute no invoca save si la clase no existe")
    void execute_doesNotSave_whenNotFound() {
        when(claseFindByIdService.execute(999L)).thenThrow(new NotFoundException("Clase no encontrada"));

        assertThatThrownBy(() -> claseDeleteService.execute(999L))
                .isInstanceOf(NotFoundException.class);

        verify(claseRepository, never()).save(any());
    }
}
