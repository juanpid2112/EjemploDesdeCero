package com.prog4.EjemploDesdeCero.features.clases.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import com.prog4.EjemploDesdeCero.features.clases.models.Clase;

@DataJpaTest
@ActiveProfiles("test")
class IClaseRepositoryTest {

    @Autowired
    private IClaseRepository claseRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("save y findById persisten y recuperan la entidad")
    void save_andFindById_roundTrip() {
        Clase toSave = Clase.builder()
                .name("Funcional")
                .instructor("I1")
                .maxCapacity(10)
                .deleted(false)
                .build();

        Clase saved = claseRepository.save(toSave);
        entityManager.flush();
        entityManager.clear();

        Optional<Clase> found = claseRepository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Funcional");
    }

    @Test
    @DisplayName("findByDeletedFalse excluye clases con deleted=true")
    void findByDeletedFalse_excludesDeleted() {
        Clase active = Clase.builder()
                .name("Activa")
                .instructor("A")
                .maxCapacity(10)
                .deleted(false)
                .build();
        Clase removed = Clase.builder()
                .name("Borrada")
                .instructor("B")
                .maxCapacity(12)
                .deleted(true)
                .build();
        claseRepository.save(active);
        claseRepository.save(removed);
        entityManager.flush();
        entityManager.clear();

        List<Clase> result = claseRepository.findByDeletedFalse();

        assertThat(result).extracting(Clase::getName).containsExactly("Activa");
    }

    @Test
    @DisplayName("findByIdAndDeletedFalse devuelve vacío si la clase está borrada lógicamente")
    void findByIdAndDeletedFalse_emptyWhenDeleted() {
        Clase removed = Clase.builder()
                .name("X")
                .instructor("Y")
                .maxCapacity(20)
                .deleted(true)
                .build();
        Clase saved = claseRepository.save(removed);
        entityManager.flush();
        entityManager.clear();

        Optional<Clase> found = claseRepository.findByIdAndDeletedFalse(saved.getId());

        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("findByIdAndDeletedFalse devuelve vacío para id inexistente")
    void findByIdAndDeletedFalse_emptyWhenMissing() {
        Optional<Clase> found = claseRepository.findByIdAndDeletedFalse(9_999_999L);

        assertThat(found).isEmpty();
    }
}
