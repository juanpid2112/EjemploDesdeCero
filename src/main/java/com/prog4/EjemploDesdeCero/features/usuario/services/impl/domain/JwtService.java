package com.prog4.EjemploDesdeCero.features.usuario.services.impl.domain;

import java.util.Date;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.prog4.EjemploDesdeCero.features.usuario.services.interfaces.domain.IJwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class JwtService implements IJwtService {

    private static final String SECRET_KEY = "tu_clave_secreta_prolija_y_segura";

    @Override
    public String generateToken(UserDetails user) {
        // Este metodo se encarga de generar el token
        // Para ello hace:
        // 1. Establecer el subject del token
        // 2. Establecer la fecha de emision del token
        // 3. Establecer la fecha de expiracion del token
        // 4. Establecer la firma del token
        // 5. Compactar el token
        return Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24hs
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .compact();
    }

    @Override
    public String extractUsername(String token) {
        // Este metodo se encarga de extraer el username del token
        // Para ello hace:
        // 1. Obtener los claims del token
        // 2. Obtener el subject del token
        return getClaims(token).getSubject();
    }

    @Override
    public boolean isTokenValid(String token, UserDetails user) {
        // Este metodo se encarga de verificar si el token es valido
        // Para ello hace:
        // 1. Extraer el username del token
        // 2. Verificar si el username es igual al username del usuario
        // 3. Verificar si el token ha expirado
        final String username = extractUsername(token);
        return (username.equals(user.getUsername()) && !isTokenExpired(token));
    }

    private Claims getClaims(String token) {
        // Este metodo se encarga de obtener los claims del token
        // Para ello hace:
        // 1. Parsear el token
        // 2. Verificar la firma del token
        // 3. Obtener los claims del token
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    
    private boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    
}
