package com.example.demo.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.AlumnoRequest;
import com.example.demo.dto.AlumnoResponse;
import com.example.demo.dto.PaginaResponse;
import com.example.demo.service.AlumnoService;

import jakarta.validation.Valid;

/** CRUD de alumnos. Todas las rutas requieren un JWT válido. */
@RestController
@RequestMapping("/api/alumnos")
public class AlumnoRestController {

    private final AlumnoService alumnoService;

    public AlumnoRestController(AlumnoService alumnoService) {
        this.alumnoService = alumnoService;
    }

    @GetMapping
    public ResponseEntity<PaginaResponse<AlumnoResponse>> listar(
            @RequestParam(name = "carreraId", required = false) Long carreraId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size) {
        return ResponseEntity.ok(alumnoService.listar(carreraId, page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlumnoResponse> obtener(@PathVariable("id") Long id) {
        return ResponseEntity.ok(alumnoService.obtener(id));
    }

    @PostMapping
    public ResponseEntity<AlumnoResponse> crear(@Valid @RequestBody AlumnoRequest request) {
        AlumnoResponse creado = alumnoService.crear(request);
        return ResponseEntity.created(URI.create("/api/alumnos/" + creado.id())).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlumnoResponse> actualizar(@PathVariable("id") Long id,
            @Valid @RequestBody AlumnoRequest request) {
        return ResponseEntity.ok(alumnoService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) {
        alumnoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
