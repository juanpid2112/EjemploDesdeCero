package com.prog4.EjemploDesdeCero.features.clases.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prog4.EjemploDesdeCero.configs.BaseResponse;
import com.prog4.EjemploDesdeCero.features.clases.dtos.request.ClaseCreateRequestDto;
import com.prog4.EjemploDesdeCero.features.clases.dtos.response.ClaseResponseDto;
import com.prog4.EjemploDesdeCero.features.clases.services.interfaces.domain.IClaseCreateService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/clases")
@AllArgsConstructor
public class ClaseController {

    private final IClaseCreateService claseCreateService;

    @PostMapping
    public ResponseEntity<BaseResponse<ClaseResponseDto>> createClase(
        @Valid @RequestBody ClaseCreateRequestDto request
    ) {
        return ResponseEntity.ok(
            BaseResponse.ok(
                claseCreateService.execute(request), 
                "Clase creada correctamente"
            )
        );
    }
}
