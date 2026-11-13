package com.example.demo.security;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import jakarta.servlet.http.HttpServletResponse;

/** Escribe errores JSON desde los filtros de seguridad (donde no actúa @RestControllerAdvice). */
final class RespuestaJson {

    private RespuestaJson() {
    }

    static void escribir(HttpServletResponse response, HttpStatus estado, String mensaje) throws IOException {
        response.setStatus(estado.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        String json = "{\"timestamp\":\"" + Instant.now() + "\","
                + "\"status\":" + estado.value() + ","
                + "\"error\":\"" + estado.getReasonPhrase() + "\","
                + "\"mensaje\":\"" + mensaje.replace("\\", "\\\\").replace("\"", "\\\"") + "\","
                + "\"errores\":{}}";
        response.getWriter().write(json);
    }
}
