package vn.job.service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vn.job.model.Token;
import vn.job.repository.TokenRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;

    public Token saveToken(Token token) {
        Optional<Token> tokenOptional = this.tokenRepository.findByEmail(token.getEmail());
        if(tokenOptional.isEmpty()) {
           return this.tokenRepository.save(token);
        }
        else {
           Token currentToken = tokenOptional.get();
           currentToken.setAccessToken(token.getAccessToken());
           currentToken.setRefreshToken(token.getRefreshToken());
           return this.tokenRepository.save(currentToken);
        }

    }

    public String deleteToken(Token token) {
        this.tokenRepository.delete(token);
        return "Token deleted successfully";
    }

    public Token findTokenByEmail(String email) {
        return this.tokenRepository.findByEmail(email).orElseThrow(() ->new UsernameNotFoundException("Email not found"));
    }

    public Token findTokenByAccessToken(String accessToken) {
        return this.tokenRepository.findByAccessToken(accessToken).orElseThrow(() -> new UsernameNotFoundException("Token not found")) ;// Tìm token theo accessToken
    }

    public Token findTokenByRefreshToken(String accessToken) {
        return this.tokenRepository.findByRefreshToken(accessToken).orElseThrow(() -> new UsernameNotFoundException("Token not found")) ;// Tìm token theo accessToken
    }

}
