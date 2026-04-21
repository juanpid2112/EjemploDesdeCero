package com.prog4.EjemploDesdeCero.features.clases.models;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "gym_classes")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Clase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    private String name;

    private String instructor;

    @Min(5)
    @Max(50)
    private Integer maxCapacity;

    private LocalDateTime startTime;

    @Builder.Default
    private boolean deleted = false;
    
}
