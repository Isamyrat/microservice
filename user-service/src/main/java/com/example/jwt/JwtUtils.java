package com.example.jwt;

import com.example.exception.UnauthorizedException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;


@Component
public class JwtUtils {

    @Value("${jwt.expiration-time}")
    private int expirationTime;
    @Value("${jwt.SECRET}")
    private String SECRET;
    public static final String BEARER_PREFIX = "Bearer ";

    public String getJwtToken(String username) {
        return generateToken(username,
                             SECRET,
                             expirationTime);

    }

    public static String generateToken(String username, String secret, int expirationTime) {
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(new Date())
            .setExpiration(new Date(new Date().getTime() + expirationTime))
            .signWith(SignatureAlgorithm.HS512, secret)
            .compact();
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody().getSubject();
    }

    public void validateJwtToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException("Authentication token is expired.");
        }
    }

    public String parseJwt(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(header) && header.startsWith(BEARER_PREFIX)) {
            return header.substring(7);
        } else {
            throw new UnauthorizedException("Invalid token.");
        }
    }
}
