package com.sampi.prestamoservice.controller;

import com.sampi.prestamoservice.dto.PrestamoRequest;
import com.sampi.prestamoservice.dto.PrestamoResponse;
import com.sampi.prestamoservice.dto.ResumenCarteraResponse;
import com.sampi.prestamoservice.service.PrestamoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/prestamos")
public class PrestamoController {

    private final PrestamoService prestamoService;

    public PrestamoController(PrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    @GetMapping
    public ResponseEntity<List<PrestamoResponse>> listar() {
        return ResponseEntity.ok(prestamoService.listar());
    }

    @GetMapping("/resumen")
    public ResponseEntity<ResumenCarteraResponse> obtenerResumen() {
        return ResponseEntity.ok(prestamoService.obtenerResumen());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrestamoResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(prestamoService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<PrestamoResponse> crear(@Valid @RequestBody PrestamoRequest request) {
        PrestamoResponse creado = prestamoService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PrestamoResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody PrestamoRequest request) {
        return ResponseEntity.ok(prestamoService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        prestamoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
