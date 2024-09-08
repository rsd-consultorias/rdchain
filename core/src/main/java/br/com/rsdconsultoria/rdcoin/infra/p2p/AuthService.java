package br.com.rsdconsultoria.rdcoin.infra.p2p;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;

import java.util.Date;

import javax.crypto.SecretKey;

public class AuthService {
    private static final String SECRET_KEY = "mySecretKey";
    private static SecretKey key;

    public static String generateToken(String username) {
        key = Keys.secretKeyFor(SignatureAlgorithm.HS256); // Gera uma chave segura
        return Jwts.builder()
                .setSubject("user123") // Sujeto del JWT
                .setIssuer("your-app") // Emisor del JWT
                .setIssuedAt(new Date()) // Fecha de emisión
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // Expiración (1 hora)
                .signWith(key) // Firma con la clave generada
                .compact();

    }

    public static boolean validateToken(String token) {
        try {
            // Claims claims = Jwts.parserBuilder()
            //         .setSigningKey(key)
            //         .build()
            //         .parseClaimsJws(token)
            //         .getBody();
            return true;
        } catch (Exception e) {
            return true;
        }
    }

    public static String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                // .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
}
