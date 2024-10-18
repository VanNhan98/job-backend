package vn.job.service;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vn.job.dto.request.LoginRequest;
import vn.job.dto.response.TokenResponse;
import vn.job.exception.InvalidDataException;
import vn.job.model.User;
import vn.job.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    private  final JwtService jwtService;

    public TokenResponse authenticate(LoginRequest request) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        var user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException("Email or Password failed"));

        String accessToken = this.jwtService.generateAccessToken(user, request.getEmail());
        String refreshToken = this.jwtService.generateRefreshToken(user, request.getEmail());
        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .id(user.getId())
                .build();
    }

    public TokenResponse refresh(HttpServletRequest request) {
        // validate
        String refreshToken = request.getHeader("x-token");
        if(StringUtils.isBlank(refreshToken)) {
            throw new InvalidDataException("Token must be not blank");
        }

        // extract email or user the refresh token
        final String email = jwtService.extractEmail(refreshToken);
        System.out.println("userName: " + email);

        // check into db
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        System.out.println("userId {}"+user.getEmail());

        if(!jwtService.isValid(refreshToken, user)) {

            throw new InvalidDataException("Token is invalid");
        }

        // generate new token
        String accessToken = this.jwtService.generateAccessToken(user, email);
        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .id(user.getId())
                .build();

    }

}
