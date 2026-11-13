package com.example.demo.dto;

/** Datos públicos del usuario. Nunca incluye el password. */
public record UsuarioResponse(Long id, String nombre, String correo, String rol) {
}
