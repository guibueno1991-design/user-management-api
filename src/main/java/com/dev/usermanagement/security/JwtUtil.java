package com.dev.usermanagement.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // Chave secreta gerada para assinar o token
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    
    // Tempo de expiração do token: 1 hora (em milissegundos)
    private final long expiration = 3600000;

    // Gerar o token JWT para o usuário
    public String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    }

    // Extrair o username do token
    public String getUsernameFromToken(String token) {
        return getClaims(token).getSubject();
    }

    // Extrair o perfil/role do token
    public String getRoleFromToken(String token) {
        return getClaims(token).get("role", String.class);
    }

    // Validar se o token é válido e não expirou
    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}