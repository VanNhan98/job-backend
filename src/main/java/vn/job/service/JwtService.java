package vn.job.service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

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

    public String generateToken(UserDetails user, String email) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email); // Thêm email vào claims
        return generateToken(claims, user);
    }


    private String generateToken(Map<String, Object> claims, UserDetails user) {

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiryMinutes * 24 * 60 * 1000 * expiryDay))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getKey() {
        byte[] bytes = Decoders.BASE64.decode(accessKey);
        return Keys.hmacShaKeyFor(bytes);
    }

    public String extractEmail(String token) {
//        return extractClaim(token, Claims::getSubject);
        final Claims claims = extraAllClaim(token);
        return claims.get("email", String.class);  // Trích xuất email từ claims
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);

    }



    public boolean isValid(String token, UserDetails user) {
        final String userNameFromToken  = extractUsername(token);
        return userNameFromToken.equals(user.getUsername());
    }


    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extraAllClaim(token);
        return claimResolver.apply(claims);
    }

    private Claims extraAllClaim(String token) {
        return Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token).getBody();
    }



}
