package com.example.demo.security;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/** Se ejecuta cuando alguien llama a una ruta protegida sin token válido: responde 401 en JSON. */
@Component
public class ApiAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException {
        RespuestaJson.escribir(response, HttpStatus.UNAUTHORIZED,
                "Token ausente, inválido o vencido. Inicia sesión en /api/auth/login.");
    }
}
