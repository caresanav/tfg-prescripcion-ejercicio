package com.tfg.resumenapi.web;

import com.tfg.resumenapi.dto.CrearResumenRequest;
import com.tfg.resumenapi.dto.ResumenResponse;
import com.tfg.resumenapi.service.ResumenEntrenamientoService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/resumenes")
public class ResumenRestController {

    private final ResumenEntrenamientoService resumenService;

    public ResumenRestController(
            ResumenEntrenamientoService resumenService) {
        this.resumenService = resumenService;
    }

    /*
     * Guarda un nuevo resumen.
     *
     * POST http://localhost:9091/api/resumenes
     */
    @PostMapping
    public ResponseEntity<ResumenResponse> crearResumen(
            @RequestBody CrearResumenRequest request) {

        ResumenResponse resumenCreado =
                resumenService.crear(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(resumenCreado);
    }

    /*
     * Consulta un resumen mediante su token.
     *
     * GET http://localhost:9091/api/resumenes/{token}
     */
    @GetMapping("/{token}")
    public ResponseEntity<ResumenResponse> buscarPorToken(
            @PathVariable String token) {

        ResumenResponse resumen =
                resumenService.buscarPorToken(token);

        return ResponseEntity.ok(resumen);
    }
}