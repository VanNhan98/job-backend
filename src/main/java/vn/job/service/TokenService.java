package vn.job.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vn.job.model.Token;
import vn.job.repository.TokenRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {

    private final TokenRepository tokenRepository;

    public Token saveToken(Token token) {
        log.info("---------------save token---------------");
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
        log.info("---------------delete token---------------");
        this.tokenRepository.delete(token);
        return "Token deleted successfully";
    }

    public Token findTokenByEmail(String email) {
        return this.tokenRepository.findByEmail(email).orElseThrow(() ->new UsernameNotFoundException("Email not found"));
    }



}
