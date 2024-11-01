package vn.job.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    public Optional<Token> findTokenByEmail(String email) {
        return this.tokenRepository.findByEmail(email);
    }


    public void updateToken(Token token) {
        log.info("---------------update token---------------");
        Optional<Token> existingToken = findTokenByEmail(token.getEmail());
        if (existingToken.isPresent()) {
            Token oldToken = existingToken.get();
            oldToken.setAccessToken(token.getAccessToken());
            oldToken.setRefreshToken(token.getRefreshToken());
            saveToken(oldToken); // cập nhật token đã tồn tại
        } else {
            saveToken(token); // lưu mới nếu chưa có
        }
    }

}
