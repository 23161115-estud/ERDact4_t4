package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** Lo que el cliente puede enviar para crear o actualizar un alumno. */
public record AlumnoRequest(
        @NotBlank(message = "El nombre es obligatorio.")
        @Size(max = 100, message = "El nombre no puede pasar de 100 caracteres.")
        String nombre,

        @NotBlank(message = "El correo es obligatorio.")
        @Email(message = "Escribe un correo válido.")
        @Size(max = 120, message = "El correo no puede pasar de 120 caracteres.")
        String correo,

        @NotNull(message = "El semestre es obligatorio.")
        @Min(value = 1, message = "El semestre debe estar entre 1 y 12.")
        @Max(value = 12, message = "El semestre debe estar entre 1 y 12.")
        Integer semestre,

        @NotNull(message = "Selecciona una carrera (carreraId).")
        Long carreraId) {
}
