package com.prog4.EjemploDesdeCero.features.clases.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prog4.EjemploDesdeCero.configs.BaseResponse;
import com.prog4.EjemploDesdeCero.configs.OpenApiConfig;
import com.prog4.EjemploDesdeCero.features.clases.dtos.request.ClaseCreateRequestDto;
import com.prog4.EjemploDesdeCero.features.clases.dtos.request.ClasePatchRequestDto;
import com.prog4.EjemploDesdeCero.features.clases.dtos.response.ClaseResponseDto;
import com.prog4.EjemploDesdeCero.features.clases.services.interfaces.domain.IClaseCreateService;
import com.prog4.EjemploDesdeCero.features.clases.services.interfaces.domain.IClaseListService;
import com.prog4.EjemploDesdeCero.features.clases.services.interfaces.domain.IClasePatchService;
import com.prog4.EjemploDesdeCero.features.clases.services.interfaces.domain.IClaseDeleteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Tag(name = "Clases", description = "ABM de clases. Requiere JWT. GET: roles CLIENT o ADMIN; demás operaciones: solo ADMIN.")
@RestController
@RequestMapping("/api/clases")
@SecurityRequirement(name = OpenApiConfig.BEARER_JWT_SCHEME)
@AllArgsConstructor
public class ClaseController {

    private final IClaseCreateService claseCreateService;

    private final IClaseListService claseListService;

    private final IClasePatchService clasePatchService;

    private final IClaseDeleteService claseDeleteService;

    @Operation(summary = "Crear clase", description = "Registra una nueva clase. Requiere rol ADMIN.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Clase creada correctamente"),
        @ApiResponse(responseCode = "400", description = "Validación fallida (RFC 7807)"),
        @ApiResponse(responseCode = "401", description = "Token ausente o inválido"),
        @ApiResponse(responseCode = "403", description = "Sin permisos; se requiere rol ADMIN")
    })
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

    @Operation(summary = "Listar clases", description = "Devuelve todas las clases. Roles permitidos: CLIENT o ADMIN.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
        @ApiResponse(responseCode = "401", description = "Token ausente o inválido"),
        @ApiResponse(responseCode = "403", description = "Sin permisos para este recurso")
    })
    @GetMapping
    public ResponseEntity<BaseResponse<List<ClaseResponseDto>>> listClases() {
        return ResponseEntity.ok(
            BaseResponse.ok(
                claseListService.execute(), 
                "Clases listadas correctamente"
            )
        );
    }

    @Operation(summary = "Actualizar clase (parcial)", description = "Actualiza solo los campos enviados. Requiere rol ADMIN.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Clase actualizada correctamente"),
        @ApiResponse(responseCode = "400", description = "Validación fallida (RFC 7807)"),
        @ApiResponse(responseCode = "401", description = "Token ausente o inválido"),
        @ApiResponse(responseCode = "403", description = "Sin permisos; se requiere rol ADMIN"),
        @ApiResponse(responseCode = "404", description = "Clase no encontrada")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<BaseResponse<ClaseResponseDto>> patchClase(
        @Parameter(description = "Identificador de la clase", required = true, example = "1")
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

    @Operation(summary = "Eliminar clase", description = "Elimina una clase por id. Requiere rol ADMIN.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Clase eliminada correctamente"),
        @ApiResponse(responseCode = "401", description = "Token ausente o inválido"),
        @ApiResponse(responseCode = "403", description = "Sin permisos; se requiere rol ADMIN"),
        @ApiResponse(responseCode = "404", description = "Clase no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> deleteClase(
        @Parameter(description = "Identificador de la clase", required = true, example = "1")
        @PathVariable Long id
    ) {
        claseDeleteService.execute(id);
        return ResponseEntity.ok(
            BaseResponse.noContent(
                "Clase eliminada correctamente"
            )
        );
    }
}