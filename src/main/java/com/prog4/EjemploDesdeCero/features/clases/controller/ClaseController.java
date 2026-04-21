package com.prog4.EjemploDesdeCero.features.clases.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prog4.EjemploDesdeCero.configs.BaseResponse;
import com.prog4.EjemploDesdeCero.features.clases.dtos.request.ClaseCreateRequestDto;
import com.prog4.EjemploDesdeCero.features.clases.dtos.request.ClasePatchRequestDto;
import com.prog4.EjemploDesdeCero.features.clases.dtos.response.ClaseResponseDto;
import com.prog4.EjemploDesdeCero.features.clases.services.interfaces.domain.IClaseCreateService;
import com.prog4.EjemploDesdeCero.features.clases.services.interfaces.domain.IClaseListService;
import com.prog4.EjemploDesdeCero.features.clases.services.interfaces.domain.IClasePatchService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/clases")
@AllArgsConstructor
public class ClaseController {

    private final IClaseCreateService claseCreateService;

    private final IClaseListService claseListService;

    private final IClasePatchService clasePatchService;

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

    @GetMapping
    public ResponseEntity<BaseResponse<List<ClaseResponseDto>>> listClases() {
        return ResponseEntity.ok(
            BaseResponse.ok(
                claseListService.execute(), 
                "Clases listadas correctamente"
            )
        );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BaseResponse<ClaseResponseDto>> patchClase(
        @PathVariable Long id,
        @Valid @RequestBody ClasePatchRequestDto request
    ) {
        return ResponseEntity.ok(
            BaseResponse.ok(
                clasePatchService.execute(id, request), 
                "Clase actualizada correctamente"
            )
        );
    }
}