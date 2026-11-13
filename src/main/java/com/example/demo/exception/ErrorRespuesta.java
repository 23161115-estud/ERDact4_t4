package com.example.demo.exception;

import java.time.Instant;
import java.util.Map;

import org.springframework.http.HttpStatus;

/** Formato único de error para toda la API. */
public record ErrorRespuesta(String timestamp, int status, String error, String mensaje,
        Map<String, String> errores) {

    public static ErrorRespuesta de(HttpStatus estado, String mensaje) {
        return new ErrorRespuesta(Instant.now().toString(), estado.value(), estado.getReasonPhrase(),
                mensaje, Map.of());
    }

    public static ErrorRespuesta de(HttpStatus estado, String mensaje, Map<String, String> errores) {
        return new ErrorRespuesta(Instant.now().toString(), estado.value(), estado.getReasonPhrase(),
                mensaje, errores);
    }
}
