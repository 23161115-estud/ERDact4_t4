package com.example.demo.security;

import java.nio.charset.StandardCharsets;
import java.time.Instant;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Usuario;
import com.nimbusds.jose.jwk.source.ImmutableSecret;

/**
 * Genera y valida tokens JWT firmados con HS256 (clave secreta compartida).
 * El decoder valida la firma y la fecha de expiración automáticamente.
 */
@Service
public class JwtService {

    private final JwtEncoder encoder;
    private final JwtDecoder decoder;
    private final long expiracionSegundos;

    public JwtService(@Value("${app.jwt.secret}") String secreto,
            @Value("${app.jwt.expiracion-minutos:60}") long expiracionMinutos) {
        byte[] bytes = secreto.getBytes(StandardCharsets.UTF_8);
        if (bytes.length < 32) {
            throw new IllegalStateException("app.jwt.secret debe tener al menos 32 caracteres para HS256.");
        }
        SecretKey clave = new SecretKeySpec(bytes, "HmacSHA256");
        this.encoder = new NimbusJwtEncoder(new ImmutableSecret<>(clave));
        this.decoder = NimbusJwtDecoder.withSecretKey(clave).macAlgorithm(MacAlgorithm.HS256).build();
        this.expiracionSegundos = expiracionMinutos * 60;
    }

    public String generarToken(Usuario usuario) {
        Instant ahora = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("ERDact4_t4")
                .subject(usuario.getCorreo())
                .issuedAt(ahora)
                .expiresAt(ahora.plusSeconds(expiracionSegundos))
                .claim("rol", usuario.getRol())
                .build();
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        return encoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }

    /** Lanza JwtException si el token está mal formado, alterado o vencido. */
    public Jwt decodificar(String token) {
        return decoder.decode(token);
    }

    public long getExpiracionSegundos() {
        return expiracionSegundos;
    }
}
