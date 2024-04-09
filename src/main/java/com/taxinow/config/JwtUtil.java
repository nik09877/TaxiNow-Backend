package com.taxinow.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {
    SecretKey key = Keys.hmacShaKeyFor(JwtSecurityContext.JWT_KEY.getBytes());

    public String generateJwtToken(Authentication authentication) {

        return Jwts.builder().setIssuer("Nikhil Mishra")
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 864000000))
                .claim("email", authentication.getName())
                .signWith(key).compact();
    }

    public String getEmailFromJwt(String jwt) {
        jwt = jwt.substring(7);
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt).getBody();
//                .parseClaimsJwt(jwt).getBody();

        String email = String.valueOf(claims.get("email"));
        return email;
    }
}
