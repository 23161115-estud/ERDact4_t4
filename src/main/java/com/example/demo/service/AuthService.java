package com.example.demo.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegistroRequest;
import com.example.demo.dto.UsuarioResponse;
import com.example.demo.entity.Usuario;
import com.example.demo.exception.ConflictoException;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.security.JwtService;

@Service
public class AuthService {

    private static final String ROL_POR_DEFECTO = "USER";

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager, JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthResponse registrar(RegistroRequest request) {
        String correo = request.correo().trim().toLowerCase();
        if (usuarioRepository.existsByCorreoIgnoreCase(correo)) {
            throw new ConflictoException("Ya existe un usuario con ese correo.");
        }
        Usuario usuario = new Usuario(
                request.nombre().trim(),
                correo,
                passwordEncoder.encode(request.password()),
                ROL_POR_DEFECTO);
        return construirRespuesta(usuarioRepository.save(usuario));
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        String correo = request.correo().trim().toLowerCase();
        // Lanza BadCredentialsException (-> 401) si el correo o la contraseña no coinciden
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(correo, request.password()));
        Usuario usuario = usuarioRepository.findByCorreoIgnoreCase(correo)
                .orElseThrow(() -> new BadCredentialsException("Credenciales inválidas."));
        return construirRespuesta(usuario);
    }

    private AuthResponse construirRespuesta(Usuario usuario) {
        return new AuthResponse(
                jwtService.generarToken(usuario),
                "Bearer",
                jwtService.getExpiracionSegundos(),
                new UsuarioResponse(usuario.getId(), usuario.getNombre(), usuario.getCorreo(), usuario.getRol()));
    }
}
