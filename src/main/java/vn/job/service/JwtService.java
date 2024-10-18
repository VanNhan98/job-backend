package vn.job.service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import vn.job.model.User;
import vn.job.util.TokenType;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static vn.job.util.TokenType.ACCESS_TOKEN;
import static vn.job.util.TokenType.REFRESH_TOKEN;

@Service
public class JwtService {

    @Value("${jwt.expiryMinutes}")
    private long expiryMinutes;

    @Value("${jwt.expiryDay}")
    private long expiryDay;

    @Value("${jwt.accessKey}")
    private String accessKey;

    @Value("${jwt.refreshKey}")
    private String refreshKey;

    public String generateAccessToken(UserDetails user, String email) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);
        return generateAccessToken(claims, user);
    }


    private String generateAccessToken(Map<String, Object> claims, UserDetails user) {

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiryMinutes * 60 * 1000 * expiryDay))
                .signWith(getKey(ACCESS_TOKEN), SignatureAlgorithm.HS256)
                .compact();
    }


    public String generateRefreshToken(User user, String email) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);
        return generateRefreshToken(claims, user);
    }


    private String generateRefreshToken(Map<String, Object> claims, UserDetails user) {

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * expiryDay))
                .signWith(getKey(REFRESH_TOKEN), SignatureAlgorithm.HS256)
                .compact();
    }



    private Key getKey(TokenType type) {
        byte[] bytes;
        if(ACCESS_TOKEN.equals(type))
      bytes = Decoders.BASE64.decode(accessKey);
        else {
            bytes = Decoders.BASE64.decode(refreshKey);
        }
        return Keys.hmacShaKeyFor(bytes);
    }

    public String extractEmail(String token, TokenType type) {
        final Claims claims = extraAllClaim(token, type);
        return claims.get("email", String.class);
    }

    public String extractUsername(String token, TokenType type) {
        return extractClaim(token, type, Claims::getSubject);
    }



    public boolean isValid(String token,TokenType type, UserDetails user) {
        final String userNameFromToken  = extractUsername(token, type);
        return userNameFromToken.equals(user.getUsername()) && !isTokenExpired(token, type);
    }


    private <T> T extractClaim(String token, TokenType type,Function<Claims, T> claimResolver) {
        final Claims claims = extraAllClaim(token,type);
        return claimResolver.apply(claims);
    }

    private Claims extraAllClaim(String token, TokenType type) {
        try {
            return Jwts.parserBuilder().setSigningKey(getKey(type)).build().parseClaimsJws(token).getBody();
        } catch (SignatureException | ExpiredJwtException e) { // Invalid signature or expired token
            throw new AccessDeniedException("Access denied: " + e.getMessage());
        }
    }


    private boolean isTokenExpired(String token, TokenType type) {
        return extractExpiration(token,type).before(new Date()) ;
    }

    private Date extractExpiration(String token, TokenType type) {
        return extractClaim(token, type, Claims::getExpiration);
    }
}
