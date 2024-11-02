package vn.job.service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import vn.job.model.User;
import vn.job.util.TokenType;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static vn.job.util.TokenType.*;

@Service
@Slf4j
public class JwtService {

    @Value("${jwt.expiryMinutes}")
    private long expiryMinutes;

    @Value("${jwt.expiryDay}")
    private long expiryDay;

    @Value("${jwt.accessKey}")
    private String accessKey;

    @Value("${jwt.refreshKey}")
    private String refreshKey;

    @Value("${jwt.secretKey}")
    private String secretKey;

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
                .signWith(getKey(ACCESS_TOKEN), SignatureAlgorithm.HS512)

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
                .signWith(getKey(REFRESH_TOKEN), SignatureAlgorithm.HS512)
                .compact();
    }


    public String generateResetToken(User user, String email) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);
        return generateResetToken(claims, user);
    }

    private String generateResetToken(Map<String, Object> claims, User user) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * expiryDay))
                .signWith(getKey(RESET_TOKEN), SignatureAlgorithm.HS512)
                .compact();
    }


    private Key getKey(TokenType type) {
        switch (type) {
            case ACCESS_TOKEN:
                return Keys.hmacShaKeyFor(accessKey.getBytes());
            case REFRESH_TOKEN:
                return Keys.hmacShaKeyFor(refreshKey.getBytes());
            case RESET_TOKEN:
                return Keys.hmacShaKeyFor(secretKey.getBytes());
            default:
                throw new IllegalStateException("Unknown token type: " + type);
        }
    }

    public String extractEmail(String token, TokenType type) {
        final Claims claims = extraAllClaim(token, type);
        return claims.get("email", String.class);
    }

    public String extractUsername(String token, TokenType type) {
        return extractClaim(token, type, Claims::getSubject);
    }


    public boolean isValid(String token, TokenType type, UserDetails user) {
        final String userNameFromToken = extractUsername(token, type);
        return userNameFromToken.equals(user.getUsername()) && !isTokenExpired(token, type);
    }


    private <T> T extractClaim(String token, TokenType type, Function<Claims, T> claimResolver) {
        final Claims claims = extraAllClaim(token, type);
        return claimResolver.apply(claims);
    }

    private Claims extraAllClaim(String token, TokenType type) {
        try {
            return Jwts.parserBuilder().setSigningKey(getKey(type)).build().parseClaimsJws(token).getBody();
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
            throw new AccessDeniedException("Invalid JWT signature");
        } catch (ExpiredJwtException e) {
            log.error("JWT expired: {}", e.getMessage());
            throw new AccessDeniedException("JWT expired");
        }
    }


    private boolean isTokenExpired(String token, TokenType type) {
        return extractExpiration(token, type).before(new Date());
    }

    private Date extractExpiration(String token, TokenType type) {
        return extractClaim(token, type, Claims::getExpiration);
    }


    public static Optional<String> getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            return Optional.ofNullable(authentication.getName());
        }
        return Optional.empty();
    }

}
