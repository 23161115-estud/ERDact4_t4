package com.example.demo.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.CarreraRequest;
import com.example.demo.dto.CarreraResponse;
import com.example.demo.service.CarreraService;

import jakarta.validation.Valid;

/** Catálogo de carreras (necesario para saber qué carreraId usar al crear alumnos). */
@RestController
@RequestMapping("/api/carreras")
public class CarreraRestController {

    private final CarreraService carreraService;

    public CarreraRestController(CarreraService carreraService) {
        this.carreraService = carreraService;
    }

    @GetMapping
    public ResponseEntity<List<CarreraResponse>> listar() {
        return ResponseEntity.ok(carreraService.listar());
    }

    @PostMapping
    public ResponseEntity<CarreraResponse> crear(@Valid @RequestBody CarreraRequest request) {
        CarreraResponse creada = carreraService.crear(request);
        return ResponseEntity.created(URI.create("/api/carreras/" + creada.id())).body(creada);
    }
}
