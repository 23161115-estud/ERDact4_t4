package com.example.demo.dto;

/** Lo que la API devuelve de un alumno (incluye el nombre de su carrera). */
public record AlumnoResponse(Long id, String nombre, String correo, Integer semestre,
        Long carreraId, String carreraNombre) {
}
